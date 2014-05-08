package info.blogstack.services.dao;

import info.blogstack.entities.ImportSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ImportSourceDAOImpl extends GenericDAOImpl<ImportSource> implements ImportSourceDAO {

	@Autowired
	public ImportSourceDAOImpl(SessionFactory sessionFactory) {
		super(ImportSource.class, sessionFactory);
	}
}