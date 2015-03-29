package info.blogstack.persistence.daos;

import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;

import java.util.List;

public interface LabelDAO {

	List<LabelRecord> findAll();
	
	List<LabelRecord> findAll(Pagination pagination);

	LabelRecord findByName(String name);

	LabelRecord findByHash(String hash);
	
	List<LabelRecord> findByPost(PostRecord post, int n);
	
	List<LabelRecord> findByPost(PostRecord post, int n, boolean visible);
	
	Long countAll();
}