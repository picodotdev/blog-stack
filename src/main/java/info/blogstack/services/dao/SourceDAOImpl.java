package info.blogstack.services.dao;

import info.blogstack.entities.Source;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;

public class SourceDAOImpl extends GenericDAOImpl<Source> implements SourceDAO {

	@Autowired
	public SourceDAOImpl(SessionFactory sessionFactory) {
		super(Source.class, sessionFactory);
	}
	
	@Override
	public List<Source> findAll(Pagination pagination) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Source.class);
		criteria.add(Restrictions.eq("enabled", true));
		setPagination(criteria, pagination);
		return criteria.list();
	}
	
	@Override
	public List<Source> findLastWithPosts() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Source.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("posts", "p");
		criteria.addOrder(Order.desc("p.date"));
		criteria.setMaxResults(120);
		return criteria.list();
	}
	
	@Override
	public List<Source> findImportPending() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Source.class);
		criteria.createAlias("importSource", "is", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.or(Restrictions.isNull("importSource"), Restrictions.isNull("is.updateDate")));
		return criteria.list();
	}
}