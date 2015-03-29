package info.blogstack.persistence.daos;

import static info.blogstack.persistence.jooq.Tables.NEWSLETTER;
import info.blogstack.persistence.jooq.tables.records.NewsletterRecord;

import org.jooq.DSLContext;

public class NewsletterDAOImpl implements NewsletterDAO {

	private DSLContext context;

	public NewsletterDAOImpl(DSLContext context) {
		this.context = context;
	}
	
	@Override
	public NewsletterRecord findById(Long id) {
		return context.selectFrom(NEWSLETTER).where(NEWSLETTER.ID.eq(id)).fetchOne();
	}
}