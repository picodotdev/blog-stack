package info.blogstack.services;

import info.blogstack.entities.Post;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface IndexerService {

	@Transactional(readOnly = true)
	void setForceIndex(boolean force);
	
	@Transactional(propagation = Propagation.REQUIRED)
	List<Post> index() throws Exception;
	
	@Transactional(propagation = Propagation.REQUIRED)
	List<Post> importAll() throws Exception;
}