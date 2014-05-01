package info.blogstack.services.dao;

import info.blogstack.entities.Source;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class SourceDAOImpl extends GenericDAOImpl<Source> implements SourceDAO {

	@Autowired
	public SourceDAOImpl(SessionFactory sessionFactory) {
		super(Source.class, sessionFactory);
	}
	
	@Override
	public void persist(Source source) {
		super.persist(source);
	}

	@Override
	public List<Source> findLastWithPosts() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Source.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("posts", "p");
		criteria.addOrder(Order.desc("p.date"));
		criteria.setMaxResults(10);
		return criteria.list();
	}
	
	@Override
	public List<Source> findImportPending() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Source.class);
		criteria.createAlias("importSource", "is");
		criteria.add(Restrictions.isNotNull("importSource"));
		criteria.add(Restrictions.isNull("is.updateDate"));
		return criteria.list();
	}
}