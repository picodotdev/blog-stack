package info.blogstack.persistence.daos;

import org.jooq.DSLContext;

public class PostsIndexationsDAOImpl implements PostsIndexationsDAO {

	private DSLContext context;

	public PostsIndexationsDAOImpl(DSLContext context) {
		this.context = context;
	}
}