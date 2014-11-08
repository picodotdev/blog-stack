package info.blogstack.services;

import info.blogstack.persistence.jooq.tables.records.PostRecord;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

public interface ShareService {

	@Transactional(readOnly = true)
	void share(Collection<PostRecord> posts);
}