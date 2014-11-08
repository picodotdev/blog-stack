package info.blogstack.persistence.daos;

import info.blogstack.persistence.jooq.tables.records.SourceRecord;

import java.util.List;

public interface SourceDAO {

	List<SourceRecord> findAll();
	
	List<SourceRecord> findAll(Pagination pagination);

	List<SourceRecord> findLastWithPosts();

	List<SourceRecord> findImportPending();

	Long countAll();
}