package info.blogstack.services.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenericDAOImpl<T> implements GenericDAO<T> {

	private Class clazz;
	protected SessionFactory sessionFactory;

	public GenericDAOImpl(Class<T> clazz, SessionFactory sessionFactory) {
		this.clazz = clazz;
		this.sessionFactory = sessionFactory;
	}

	@Override
	public T findById(Serializable id) {
		return (T) sessionFactory.getCurrentSession().get(clazz, id);
	}

	@Override
	public List<T> findAll() {
		return findAll(null);
	}

	@Override
	public List<T> findAll(Pagination pagination) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
		setPagination(criteria, pagination);
		return criteria.list();
	}

	@Override
	public Long countAll() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	@Override
	public void persist(T object) {
		sessionFactory.getCurrentSession().saveOrUpdate(object);
	}

	@Override
	public void remove(T object) {
		sessionFactory.getCurrentSession().delete(object);
	}

	@Override
	public void removeAll() {
		String hql = String.format("delete from %s", clazz.getName());
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.executeUpdate();
	}
	
	protected void setPagination(Criteria criteria, Pagination pagination) {
		if (pagination != null) {
			List<Order> orders = pagination.getOrders();
			for (Order order : orders) {
				criteria.addOrder(order);
			}
		}

		if (pagination != null) {
			criteria.setFirstResult(pagination.getStart());
			criteria.setMaxResults(pagination.getEnd() - pagination.getStart());
		}
	}
}