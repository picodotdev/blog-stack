package info.blogstack.persistence.daos;

import info.blogstack.persistence.jooq.tables.records.NewsletterRecord;

public interface NewsletterDAO {

	NewsletterRecord findById(Long id);
}