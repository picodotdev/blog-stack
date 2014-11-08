package info.blogstack.components;

import info.blogstack.misc.Utils;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.services.MainService;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ShareThis {

	@Parameter(value = "false")
	@Property
	private boolean onlyTwitter;
	
	@Parameter
	@Property
	private PostRecord post;
	
	@Inject
	private MainService service;
	
	public String getClasses() {
		return (onlyTwitter) ? "share-this only-twitter": "share-this";
	}
	
	@Cached(watch = "post")
	public String getTitle() {
		if (post == null) {
			return null;
		}
		return String.format("%s | %s", post.getTitle(), post.fetchParent(Keys.POST_SOURCE_ID).getName());
	}
	
	@Cached(watch = "post")
	public String getUrl() {
		return Utils.getUrl(service, post, post.fetchParent(Keys.POST_SOURCE_ID));
	}
}