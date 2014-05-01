package info.blogstack.services.dao;

import info.blogstack.entities.Source;

import java.util.List;

public interface SourceDAO extends GenericDAO<Source> {

	List<Source> findLastWithPosts();
	List<Source> findImportPending();
}