package info.blogstack.components;

import info.blogstack.misc.Feed;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.PostsLabelsRecord;
import info.blogstack.persistence.records.AppLabelRecord;
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
	private PostRecord post;
	
	@Parameter
	@Property
	private LabelRecord label;
	
	@Property
	private List<Feed> feeds;
	
	@Property
	private Feed feed;
	
	@Inject
	private MainService service;
	
	@Inject
	private OfflineRequestGlobals requestGlobals;
	
	boolean setupRender() {
		feeds = new ArrayList<>();
		if (post != null) {
			for (PostsLabelsRecord pl : post.fetchChildren(Keys.POSTS_LABELS_POST_ID)) {
				LabelRecord l = pl.fetchParent(Keys.POSTS_LABELS_LABEL_ID);
				AppLabelRecord al = l.into(AppLabelRecord.class);
				if (!al.getEnabled()) {
					continue;
				}
				feeds.add(new Feed(requestGlobals.getContextPath() + service.getGenerateService().getToRss(al).getPath(), String.format("Fuente de la etiqueta %s", al.getName())));
			}
		} else if (label != null) {
			feeds.add(new Feed(requestGlobals.getContextPath() + service.getGenerateService().getToRss(label).getPath(), String.format("Fuente de la etiqueta %s", label.getName())));
		} else {
			feeds.add(new Feed(requestGlobals.getContextPath() + service.getGenerateService().getToRss().getPath(), "Fuente de la portada de Blog Stack"));
		}
		
		return !feeds.isEmpty();
	}
}