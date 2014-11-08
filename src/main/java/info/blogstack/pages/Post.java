package info.blogstack.pages;

import info.blogstack.misc.Utils;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.AdsenseRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;
import info.blogstack.services.MainService;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Post {

	private Object[] context;

	@Inject
	private MainService service;

	@Property
	private PostRecord post;
	
	void onActivate(Object[] context) {
		this.context = context;
	}
	
	Object[] onPassivate() {
		return context;
	}

	void setupRender() {
		String hash = Utils.getHash(context);
		post = service.getPostDAO().findByHash(hash);
	}
	
	public String getTitle() {
		return post.getTitle();
	}

	public String getSubtitle() {
		return post.fetchParent(Keys.POST_SOURCE_ID).getName();
	}
	
	public SourceRecord getSource() {
		return post.fetchParent(Keys.POST_SOURCE_ID);
	}
	
	public AdsenseRecord getAdsense() {
		return getSource().fetchParent(Keys.SOURCE_ADSENSE_ID);
	}
}