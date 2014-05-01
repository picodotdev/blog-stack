package info.blogstack.services.dao;

import java.util.ArrayList;
import java.util.List;

import info.blogstack.entities.Indexation;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class IndexationDAOImpl extends GenericDAOImpl<Indexation> implements IndexationDAO {

	@Autowired
	public IndexationDAOImpl(SessionFactory sessionFactory) {
		super(Indexation.class, sessionFactory);
	}
	
	@Override
	public Indexation findLast() {
		List<Sort> sorts = new ArrayList<>();
		sorts.add(new Sort("creationDate", Direction.DESCENDING));
		Pagination pagination = new Pagination(0, 1, sorts);
		List<Indexation> l = super.findAll(pagination);
		return (l.isEmpty()) ? null : l.get(0);
	}
}