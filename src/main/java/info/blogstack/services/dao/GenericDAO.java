package info.blogstack.services.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface GenericDAO<T> {

	T findById(Serializable id);

	@Transactional(readOnly = true)
	List<T> findAll();

	@Transactional(readOnly = true)
	List<T> findAll(Pagination paginacion);

	@Transactional(readOnly = true)
	Long countAll();

	@Transactional(propagation = Propagation.REQUIRED)
	void persist(T entity);

	@Transactional(propagation = Propagation.REQUIRED)
	void remove(T entity);

	@Transactional(propagation = Propagation.REQUIRED)
	void removeAll();
}