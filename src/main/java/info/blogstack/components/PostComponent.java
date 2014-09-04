package info.blogstack.components;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.misc.Utils;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class PostComponent {

	enum Mode {
		HOME, DEFAULT
	}
	
	@Parameter
	@Property
	private Post post;
	
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
	private Label label;
	
	@Inject
	private Block homeBlock;
	
	@Inject
	private Block defaultBlock;
	
	public Object[] getContext() {
		return Utils.getContext(post);
	}
	
	public Block getBlock() {
		switch (mode) {
			case HOME:
				return homeBlock;
			default:
				return defaultBlock;
		}
	}
	
	public boolean isShare() {
		return sharethis || karmacracy;
	}
	
	public boolean isComment() {
		return disqus && post.getSource().getDisqusShortname() != null;
	}
	
	@Cached(watch = "post")
	public boolean isContentExcerpted() {
		return (excerpt && post.getContent().length() != post.getContentExcerpt().length());
	}
}