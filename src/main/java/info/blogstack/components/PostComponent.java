package info.blogstack.components;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.misc.Utils;
import info.blogstack.services.MainService;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;

public class PostComponent {

	enum Mode {
		HOME_FEATURED, HOME, ARCHIVE, DEFAULT
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
	private MainService service;
	
	@Inject
	private Block smallBlock;
	
	@Inject
	private Block defaultBlock;
	
	public Object[] getContext() {
		return Utils.getContext(post);
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
	public List<Label> getLabels() {
		Query query = service.getSessionFactory().getCurrentSession().createQuery("select l from Post p inner join p.labels as l where p = :post and l.enabled = true order by size(l.posts) desc");
		query.setMaxResults(4);
		query.setParameter("post", post);
		return (List<Label>) query.list();
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
	
	public Object[] getContextLabel(Label label) {
		return Utils.getContext(label);
	}

	public String getImage() {
		return String.format("/assets/images/labels/%s.png", label.getName());
	}
}