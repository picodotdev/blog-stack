package info.blogstack.components;

import info.blogstack.entities.Source;
import info.blogstack.services.MainService;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;

public class LastSourcesWithPosts {

	@Property
	private List<Source> sources;
	
	@Property
	private Source source;
	
	@Inject
	private MainService service;
	
	void beginRender() {
		sources = service.getSourceDAO().findLastWithPosts();
	}
}