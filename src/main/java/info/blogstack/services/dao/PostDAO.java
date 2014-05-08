package info.blogstack.services.dao;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.entities.Source;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

public interface PostDAO extends GenericDAO<Post> {

	@Transactional(readOnly = true)
	List<Post> findAllBySource(Source source, Pagination pagination);
	
	@Transactional(readOnly = true)
	List<Post> findAllByLabel(Label label, Pagination pagination);	
	
	@Transactional(readOnly = true)
	List<Post> findAllByYearMonth(Integer year, Integer month);
	
	@Transactional(readOnly = true)
	Post findByURL(String url);
	
	@Transactional(readOnly = true)
	Post findByHash(String hash);
	
	@Transactional(readOnly = true)
	Long countBy(Source source);
	
	@Transactional(readOnly = true)
	Long countBy(Label label);
	
	@Transactional(readOnly = true)
	Long countAuthors();

	@Transactional(readOnly = true)
	List<Map> getArchive();
}