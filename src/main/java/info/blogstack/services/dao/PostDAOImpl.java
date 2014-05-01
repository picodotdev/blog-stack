package info.blogstack.services.dao;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.entities.Source;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class PostDAOImpl extends GenericDAOImpl<Post> implements PostDAO {

	@Autowired
	public PostDAOImpl(SessionFactory sessionFactory) {
		super(Post.class, sessionFactory);
	}

	@Override
	public List<Post> findAllBySource(Source source, Pagination pagination) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Post.class);
		criteria.add(Restrictions.eq("source", source));
		setPagination(criteria, pagination);
		return criteria.list();
	}

	@Override
	public List<Post> findAllByLabel(Label label, Pagination pagination) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Post.class);
		criteria.createAlias("labels", "l");
		criteria.add(Restrictions.in("l.id", Collections.singleton(label.getId())));
		setPagination(criteria, pagination);
		return criteria.list();
	}

	@Override
	public Post findByURL(String url) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Post.class);
		c.add(Restrictions.eq("url", url));
		return (Post) c.uniqueResult();
	}

	@Override
	public Post findByHash(String hash) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Post.class);
		c.add(Restrictions.eq("hash", hash));
		return (Post) c.uniqueResult();
	}

	@Override
	public Long countBy(Source source) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Post.class);
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("source", source));
		return (Long) criteria.uniqueResult();
	}

	@Override
	public Long countAuthors() {
		// ¿No se puede hacer esto con HQL?
		Query query = sessionFactory.getCurrentSession().createSQLQuery("select count(distinct(source_id, author)) from blogstack.post");
		return ((Number) query.uniqueResult()).longValue();
	}
}