package info.blogstack.persistence.daos;

import org.jooq.DSLContext;

public class ImportSourceDAOImpl implements ImportSourceDAO {

	private DSLContext context;
	
	public ImportSourceDAOImpl(DSLContext context) {
		this.context = context;
	}
}