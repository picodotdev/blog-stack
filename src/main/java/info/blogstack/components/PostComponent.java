package info.blogstack.components;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.misc.Utils;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class PostComponent {

	@Parameter
	@Property
	private Post post;
	
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
	
	public Object[] getContext() {
		return Utils.getContext(post);
	}
	
	public boolean isShare() {
		return sharethis || karmacracy;
	}
	
	public boolean isComent() {
		return disqus;
	}
	
	@Cached(watch = "post")
	public boolean isContentExcerpted() {
		return (excerpt && post.getContent().length() != post.getContentExcerpt().length());
	}
}