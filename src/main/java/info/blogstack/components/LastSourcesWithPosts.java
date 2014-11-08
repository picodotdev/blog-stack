package info.blogstack.components;

import info.blogstack.persistence.jooq.tables.records.SourceRecord;
import info.blogstack.services.MainService;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;

public class LastSourcesWithPosts {

	@Property
	private List<SourceRecord> sources;
	
	@Property
	private SourceRecord source;
	
	@Inject
	private MainService service;
	
	void beginRender() {
		sources = service.getSourceDAO().findLastWithPosts();
	}
}