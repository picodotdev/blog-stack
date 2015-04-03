package info.blogstack.services;

import static info.blogstack.persistence.jooq.Tables.NEWSLETTER;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Globals.Environment;
import info.blogstack.misc.Utils;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.NewsletterRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;

import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class ShareServiceImpl implements ShareService {

	private static Logger logger = LoggerFactory.getLogger(ShareServiceImpl.class);

	private static int NUMBER_LABELS = 3;

	private MainService service;

	private Twitter twitter;

	public ShareServiceImpl(MainService service) {
		this.service = service;

		ConfigurationBuilder cb = new ConfigurationBuilder().setOAuthConsumerKey((String) service.getConfiguration().get().get("twitter.consumerKey"))
				.setOAuthConsumerSecret((String) service.getConfiguration().get().get("twitter.consumerSecret"))
				.setOAuthAccessToken((String) service.getConfiguration().get().get("twitter.accessToken"))
				.setOAuthAccessTokenSecret((String) service.getConfiguration().get().get("twitter.accessTokenSecret"));
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}

	@Override
	public void shareTwitter(Collection<PostRecord> posts) {
		for (PostRecord post : posts) {
			try {
				// Build message
				StringBuilder message = new StringBuilder();
				if (post.getTitle().length() > 117) {
					message.append(post.getTitle().substring(0, 114) + "...");
				} else {
					message.append(post.getTitle());
				}
				SourceRecord source = post.fetchParent(Keys.POST_SOURCE_ID);
				if ((message.toString() + " vía " + source.getAlias()).length() <= 117) {
					message.append(" vía " + source.getAlias());
				}
				for (LabelRecord l : getLabels(post)) {
					String ln = l.getName().replaceAll("-", "");
					if ((message.toString() + " #" + ln).length() <= 117) {
						message.append(" #" + ln);
					} else {
						break;
					}
				}
				message.append(" " + Utils.getUrl(service, post, source));

				// Mark as shared
				post.setShared(true);
				post.store();
				
				// Share
				twitter(post, message.toString());
			} catch (TwitterException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	@Override
	public void shareNewsletter(Collection<PostRecord> posts) {
		if (posts.isEmpty()) {
			return;
		}
		
		try {
			NewsletterRecord newsletter = service.getContext().newRecord(NEWSLETTER);
			newsletter.setCreationdate(DateTime.now());
			newsletter.store();
			service.getPostDAO().updateNewsletter(posts, newsletter);

			newsletter(newsletter);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void twitter(PostRecord post, String message) throws TwitterException {
		logger.info("Tweetting {} as «{}»", post.getId(), message);

		if (Globals.environment != Environment.PRODUCTION) {
			return;
		}

		twitter.updateStatus(message);
	}
	
	private void newsletter(NewsletterRecord newsletter) throws Exception {
		logger.info("Newsletter #{} width {} posts»", newsletter.getId(), service.getPostDAO().countBy(newsletter));
		String content = service.getGenerateService().generateNewsletter(newsletter);

		if (Globals.environment != Environment.PRODUCTION) {
			return;
		}

		// Add style attribute to images, else tinyletter adds their
		Document document = Jsoup.parse(content);
		Elements imgs = document.select("img");
		imgs.attr("style", "text-decoration: none;");

		content = document.toString();

		service.getMailService().sendNewsletter(String.format("Boletín #%d de Blog Stack", newsletter.getId()), content);
	}

	public List<LabelRecord> getLabels(PostRecord post) {
		return service.getLabelDAO().findByPost(post, NUMBER_LABELS);
	}
}