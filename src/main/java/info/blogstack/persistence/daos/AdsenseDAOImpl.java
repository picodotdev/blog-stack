package info.blogstack.persistence.daos;

import org.jooq.DSLContext;

public class AdsenseDAOImpl implements AdsenseDAO {

	@SuppressWarnings("unused")
	private DSLContext context;
	
	public AdsenseDAOImpl(DSLContext context) {
		this.context = context;
	}
}