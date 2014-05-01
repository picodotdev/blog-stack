package info.blogstack.components;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.services.MainService;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.lazan.t5.offline.services.OfflineRequestGlobals;

public class Feeds {

	@Parameter
	@Property
	private Post post;
	
	@Parameter
	@Property
	private Label label;
	
	@Property
	private List<Feed> feeds;
	
	@Property
	private Feed feed;
	
	@Inject
	private MainService service;
	
	@Inject
	private OfflineRequestGlobals requestGlobals;
	
	void beginRender() {
		feeds = new ArrayList<>();
		if (post != null) {
			for (Label l : post.getLabels()) {
				feeds.add(new Feed(requestGlobals.getContextPath() + service.getPublicGeneratorService().getToRss(l).getPath(), String.format("Fuente de la etiqueta %s", l.getName())));
			}
		} else if (label != null) {
			feeds.add(new Feed(requestGlobals.getContextPath() + service.getPublicGeneratorService().getToRss(label).getPath(), String.format("Fuente de la etiqueta %s", label.getName())));
		} else {
			feeds.add(new Feed(requestGlobals.getContextPath() + service.getPublicGeneratorService().getToRss().getPath(), "Fuente de la portada de Blog Stack"));
		}
	}
	
	public class Feed {
		
		private String url;
		private String name;
		
		public Feed(String url, String name) {
			this.url = url;
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}		
	}
}