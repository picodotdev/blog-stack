package info.blogstack.persistence.daos;

import static info.blogstack.persistence.jooq.Tables.POST;
import static info.blogstack.persistence.jooq.Tables.SOURCE;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;

public class SourceDAOImpl implements SourceDAO {

	private DSLContext context;

	public SourceDAOImpl(DSLContext context) {
		this.context = context;
	}
	
	@Override
	public List<SourceRecord> findAll() {
		return context.selectFrom(SOURCE).where(SOURCE.ENABLED.isTrue()).fetch();
	}
	
	@Override
	public List<SourceRecord> findAll(Pagination pagination) {
		return context.selectFrom(SOURCE).limit(pagination.getOffset(), pagination.getNumberOfRows()).fetch();
	}

	@Override
	public List<SourceRecord> findLastWithPosts() {
		List<SourceRecord> s = context.select().from(SOURCE).join(POST).on(SOURCE.ID.eq(POST.SOURCE_ID)).orderBy(POST.DATE.desc(), POST.ID.desc()).limit(0, 60).fetchInto(SOURCE);
		List<SourceRecord> r = new ArrayList<>();
		for (SourceRecord o : s) {
			if (!r.contains(o)) {
				r.add(o);
			}
		}
		return r;
	}

	@Override
	public List<SourceRecord> findImportPending() {
		return context.selectFrom(SOURCE).where(SOURCE.IMPORTSOURCE_ID.isNull()).fetch();
	}

	@Override
	public Long countAll() {
		return context.selectCount().from(SOURCE).where(SOURCE.ENABLED.isTrue()).fetchOne(0, Long.class);
	}
}