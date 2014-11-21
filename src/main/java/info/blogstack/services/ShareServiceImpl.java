package info.blogstack.services;

import info.blogstack.misc.Globals;
import info.blogstack.misc.Globals.Environment;
import info.blogstack.misc.Utils;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;

import java.util.Collection;
import java.util.List;

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

		ConfigurationBuilder cb = new ConfigurationBuilder().setOAuthConsumerKey((String) service.getConfiguracion().get().get("consumerKey"))
				.setOAuthConsumerSecret((String) service.getConfiguracion().get().get("consumerSecret"))
				.setOAuthAccessToken((String) service.getConfiguracion().get().get("accessToken"))
				.setOAuthAccessTokenSecret((String) service.getConfiguracion().get().get("accessTokenSecret"));
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}

	@Override
	public void share(Collection<PostRecord> posts) {
		for (PostRecord p : posts) {
			try {
				share(p);
			} catch (TwitterException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}

	private void share(PostRecord post) throws TwitterException {
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

		// Share
		twitter(post, message.toString());
	}

	private void twitter(PostRecord post, String message) throws TwitterException {
		logger.info("Tweetting {} as «{}»", post.getId(), message);

		if (Globals.environment != Environment.PRODUCTION) {
			return;
		}

		twitter.updateStatus(message);
	}

	public List<LabelRecord> getLabels(PostRecord post) {
		return service.getLabelDAO().findByPost(post, NUMBER_LABELS);
	}
}