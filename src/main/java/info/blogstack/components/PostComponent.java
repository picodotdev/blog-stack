package info.blogstack.components;

import info.blogstack.misc.Utils;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.records.AppPostRecord;
import info.blogstack.services.MainService;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class PostComponent {

	enum Mode {
		HOME_FEATURED, HOME, ARCHIVE, DEFAULT
	}
	
	private static int NUMBER_LABELS = 4;
	
	@Parameter
	@Property
	private PostRecord post;
	
	@Parameter(value = "default", defaultPrefix = BindingConstants.LITERAL)
	@Property
	private Mode mode;
	
	@Parameter(value = "false")
	@Property
	private boolean excerpt;
	
	@Parameter(value = "false")
	@Property
	private Boolean sharethis;
	
	@Parameter(value = "false")
	@Property
	private Boolean karmacracy;
	
	@Parameter(value = "false")
	@Property
	private Boolean disqus;
	
	@Property
	private LabelRecord label;
	
	@Inject
	private MainService service;
	
	@Inject
	private Block smallBlock;
	
	@Inject
	private Block defaultBlock;
	
	public Object[] getContext() {
		return Utils.getContext(post, post.fetchParent(Keys.POST_SOURCE_ID));
	}
	
	public Block getBlock() {
		switch (mode) {
			case HOME_FEATURED:
			case HOME:
			case ARCHIVE:
				return smallBlock;
			default:
				return defaultBlock;
		}
	}
	
	public String getHomeClasses() {
		switch (mode) {
			case HOME:
				return "not-featured";
			default:
				return null;
		}
	}
	
	@Cached(watch = "post")
	public List<LabelRecord> getLabels() {
		return service.getLabelDAO().findByPost(post, NUMBER_LABELS);
	}
	
	public boolean isShare() {
		return sharethis || karmacracy;
	}
	
	public boolean isComment() {
		return disqus && post.fetchParent(Keys.POST_SOURCE_ID).getDisqusshortname() != null;
	}
	
	@Cached(watch = "post")
	public boolean isContentExcerpted() {
		AppPostRecord apost = post.into(AppPostRecord.class);
		return (excerpt && apost.getContent().length() != apost.getContentExcerpt().length());
	}
	
	@Cached(watch = "post")
	public String getContentExcerpt() {
		AppPostRecord apost = post.into(AppPostRecord.class);
		return apost.getContentExcerpt();
	}
	
	@Cached(watch = "post")
	public String getContent() {
		AppPostRecord apost = post.into(AppPostRecord.class);
		return apost.getContent();
	}
	
	public Object[] getContextLabel(LabelRecord label) {
		return Utils.getContext(label);
	}

	public String getImage() {
		return String.format("/assets/images/labels/%s.png", label.getName());
	}
}