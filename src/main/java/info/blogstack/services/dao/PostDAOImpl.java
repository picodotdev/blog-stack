package info.blogstack.services.dao;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.entities.Source;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;

public class PostDAOImpl extends GenericDAOImpl<Post> implements PostDAO {

	@Autowired
	public PostDAOImpl(SessionFactory sessionFactory) {
		super(Post.class, sessionFactory);
	}
	
	@Override
	public List<Post> findAll(Pagination pagination) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Post.class);
		criteria.setFetchMode("labels", FetchMode.SELECT);
		criteria.add(Restrictions.eq("visible", true));
		setPagination(criteria, pagination);
		return criteria.list();
	}

	@Override
	public List<Post> findAllBySource(Source source, Pagination pagination) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Post.class);
		criteria.setFetchMode("labels", FetchMode.SELECT);
		criteria.add(Restrictions.eq("visible", true));
		criteria.add(Restrictions.eq("source", source));
		setPagination(criteria, pagination);
		return criteria.list();
	}
	
	@Override
	public List<Post> findAllByYearMonth(Integer year, Integer month) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Post.class);
		criteria.setFetchMode("labels", FetchMode.SELECT);
		criteria.add(Restrictions.eq("visible", true));
		criteria.add(Restrictions.sqlRestriction("year({alias}.publishDate) = ?", year, IntegerType.INSTANCE));
		criteria.add(Restrictions.sqlRestriction("month({alias}.publishDate) = ?", month, IntegerType.INSTANCE));
		criteria.addOrder(Order.desc("publishDate"));
		return criteria.list();
	}

	@Override
	public List<Post> findAllByLabel(Label label, Pagination pagination) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Post.class);
		criteria.setFetchMode("labels", FetchMode.SELECT);
		criteria.createAlias("labels", "l");
		criteria.add(Restrictions.eq("visible", true));
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
		criteria.add(Restrictions.eq("visible", true));
		criteria.add(Restrictions.eq("source", source));
		return (Long) criteria.uniqueResult();
	}
	
	@Override
	public Long countBy(Label label) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Post.class);
		criteria.createAlias("labels", "l");
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("visible", true));
		criteria.add(Restrictions.in("l.id", Collections.singleton(label.getId())));
		return (Long) criteria.uniqueResult();
	}

	@Override
	public Long countAuthors() {
		// TODO: ¿No se puede hacer esto con HQL?
		Query query = sessionFactory.getCurrentSession().createSQLQuery("select count(distinct(source_id, author)) from blogstack.post");
		return ((Number) query.uniqueResult()).longValue();
	}
	
	@Override
	public List<Map> getArchive() {
		Query query = sessionFactory.getCurrentSession().createQuery("select new map(year(p.publishDate) as year, month(p.publishDate) as month, count(*) as posts) from Post p where p.visible = true group by year(p.publishDate), month(p.publishDate) order by year(p.publishDate) desc, month(p.publishDate) desc");
		return (List<Map>) query.list();
	}
}