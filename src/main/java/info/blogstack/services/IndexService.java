package info.blogstack.services;

import info.blogstack.entities.Post;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface IndexService {

	@Transactional(readOnly = true)
	void setForceIndex(boolean force);
	
	@Transactional(readOnly = true)
	void setForceImport(boolean force);
	
	@Transactional(propagation = Propagation.REQUIRED)
	void hash() throws Exception;
	
	@Transactional(propagation = Propagation.REQUIRED)
	List<Post> index() throws Exception;
	
	@Transactional(propagation = Propagation.REQUIRED)
	List<Post> importSources() throws Exception;
}