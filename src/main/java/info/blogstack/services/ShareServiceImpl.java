package info.blogstack.services;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Globals.Environment;
import info.blogstack.misc.Utils;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class ShareServiceImpl implements ShareService {

	private static Logger logger = LoggerFactory.getLogger(ShareServiceImpl.class);
	
	private MainService service;

	private Twitter twitter;

	public ShareServiceImpl(MainService service) {
		this.service = service;
		
		ConfigurationBuilder cb = new ConfigurationBuilder()
			.setOAuthConsumerKey((String) service.getConfiguracion().get().get("consumerKey"))
			.setOAuthConsumerSecret((String) service.getConfiguracion().get().get("consumerSecret"))
			.setOAuthAccessToken((String) service.getConfiguracion().get().get("accessToken"))
			.setOAuthAccessTokenSecret((String) service.getConfiguracion().get().get("accessTokenSecret"));
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}

	@Override
	public void share(Collection<Post> posts) {
		for(Post p : posts) {
			try {
				share(p);
			} catch (TwitterException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}

	private void share(Post post) throws TwitterException {
		// Build message
		StringBuilder message = new StringBuilder();		
		if (post.getTitle().length() > 117) {
			message.append(post.getTitle().substring(0, 114) + "...");
		} else {
			message.append(post.getTitle());
		}
		if ((message.toString() + " vía " + post.getSource().getAlias()).length() <= 117) {
			message.append(" vía " + post.getSource().getAlias());
		}
		for (Label l : getLabels(post)) {
			String ln = l.getName().replaceAll("-", "");
			if ((message.toString() + " #" + ln).length() <= 117) {
				message.append(" #" + ln);
			} else {
				break;
			}
		}
		message.append(" " + Utils.getUrl(service, post));

		// Share
		twitter(post, message.toString());
	}

	private void twitter(Post post, String message) throws TwitterException {
		logger.info("Tweetting {} as «{}»", post.getId(), message);
		
		if (Globals.environment != Environment.PRODUCTION) {
			return;
		}
		
		Status status = twitter.updateStatus(message);
	}
	
	public List<Label> getLabels(Post post) {
		Query query = service.getSessionFactory().getCurrentSession().createQuery("select l from Post p inner join p.labels as l where p = :post and l.enabled = true order by size(l.posts) desc");
		query.setMaxResults(3);
		query.setParameter("post", post);
		return (List<Label>) query.list();
	}
}