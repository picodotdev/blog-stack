package info.blogstack.persistence.daos;

import info.blogstack.persistence.jooq.tables.records.PostRecord;

public interface PostsLabelsDAO {

	int deleteByPost(PostRecord post);
}