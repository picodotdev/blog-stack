package info.blogstack.persistence.daos;

import info.blogstack.persistence.jooq.tables.records.IndexationRecord;

public interface IndexationDAO {

	IndexationRecord findLast();
}