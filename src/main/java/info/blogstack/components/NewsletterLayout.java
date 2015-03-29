package info.blogstack.components;

import info.blogstack.persistence.jooq.tables.records.NewsletterRecord;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class NewsletterLayout {

	@Parameter(defaultPrefix = BindingConstants.PROP)
	private  NewsletterRecord newsletter;
	
	@Property
	private String page;	
	
	@Inject
	ComponentResources resources;
	
	void setupRender() {
		page = resources.getPageName();
	}
	
	public String getTitle() {
		return String.format("Newsletter #%d", newsletter.getId());			
	}
}