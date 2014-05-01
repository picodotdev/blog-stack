package info.blogstack.services.dao;

import info.blogstack.entities.Post;
import info.blogstack.entities.Source;

import java.io.File;

public interface RepositoryDAO {

	File getFile(Post post);
	String getContent(Post post);
	
	File persist(Source source);
	File persist(Post post);
}