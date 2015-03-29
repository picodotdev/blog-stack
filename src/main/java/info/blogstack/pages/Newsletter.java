package info.blogstack.pages;

import static info.blogstack.persistence.jooq.Tables.NEWSLETTER;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.NewsletterRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;
import info.blogstack.persistence.records.AppPostRecord;
import info.blogstack.services.MainService;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Newsletter {

	private DateTimeFormatter MICRODATA_DATETIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm ZZZ");
	
	@Property
	private  NewsletterRecord newsletter;
	
	@Property
	private AppPostRecord post;
	
	@Property
	private Map<String, Object> archive;
	
	@Inject
	private MainService service;
	
	void onActivate(Long id) {
		newsletter = service.getNewsletterDAO().findById(id);
	}
	
	public List<AppPostRecord> getPosts() {
		return newsletter.fetchChildren(Keys.POST_NEWSLETTER_ID).into(AppPostRecord.class);
	}
	
	public List<Map<String, Object>> getLabels() {
		return service.getPostDAO().getArchiveByNewsletter(newsletter);
	}
	
	public LabelRecord getLabel() {
		return (LabelRecord) archive.get("LABEL");
	}
	
	public SourceRecord getSource() {
		return post.fetchParent(Keys.POST_SOURCE_ID);
	}
	
	public String getPreviousNewsletterDate() {
		NewsletterRecord newsletter = service.getContext().selectFrom(NEWSLETTER).orderBy(NEWSLETTER.CREATIONDATE.desc()).limit(1,1).fetchOne();
		DateTime date = (newsletter == null) ? DateTime.now() : newsletter.getCreationdate();
		return MICRODATA_DATETIME_FORMATTER.print(date);
	}
}