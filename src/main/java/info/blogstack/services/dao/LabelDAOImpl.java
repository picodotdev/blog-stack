package info.blogstack.services.dao;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class LabelDAOImpl extends GenericDAOImpl<Label> implements LabelDAO {

	@Autowired
	public LabelDAOImpl(SessionFactory sessionFactory) {
		super(Label.class, sessionFactory);
	}
	
	@Override
	public List<Label> findAll(Pagination pagination) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Label.class);
		criteria.add(Restrictions.eq("enabled", true));
		setPagination(criteria, pagination);
		return criteria.list();
	}
	
	@Override
	public Label findByName(String name) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Label.class);
		criteria.add(Restrictions.eq("name", name));
		return (Label) criteria.uniqueResult();
	}
	
	@Override
	public Label findByHash(String hash) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Label.class);
		criteria.add(Restrictions.eq("hash", hash));
		return (Label) criteria.uniqueResult();
	}
	
	@Override
	public Long countAll() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Label.class);
		criteria.add(Restrictions.eq("enabled", true));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
}