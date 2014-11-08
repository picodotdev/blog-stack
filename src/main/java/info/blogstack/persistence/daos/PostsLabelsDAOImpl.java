package info.blogstack.persistence.daos;

import static info.blogstack.persistence.jooq.Tables.POSTS_LABELS;
import info.blogstack.persistence.jooq.tables.records.PostRecord;

import org.jooq.DSLContext;

public class PostsLabelsDAOImpl implements PostsLabelsDAO {

	private DSLContext context;

	public PostsLabelsDAOImpl(DSLContext context) {
		this.context = context;
	}
	
	@Override
	public int deleteByPost(PostRecord post) {
		return context.delete(POSTS_LABELS).where(POSTS_LABELS.POST_ID.eq(post.getId())).execute();
	}
}