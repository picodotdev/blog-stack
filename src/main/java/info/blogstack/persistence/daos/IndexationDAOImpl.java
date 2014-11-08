package info.blogstack.persistence.daos;

import static info.blogstack.persistence.jooq.Tables.INDEXATION;

import org.jooq.DSLContext;

import info.blogstack.persistence.jooq.tables.records.IndexationRecord;

public class IndexationDAOImpl implements IndexationDAO {

	private DSLContext context;
	
	public IndexationDAOImpl(DSLContext context) {
		this.context = context;
	}
	
	@Override
	public IndexationRecord findLast() {		
		return context.selectFrom(INDEXATION).orderBy(INDEXATION.CREATIONDATE.desc()).limit(1).fetchOne();		
	}
}