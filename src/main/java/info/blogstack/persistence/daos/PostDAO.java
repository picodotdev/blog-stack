package info.blogstack.persistence.daos;

import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.NewsletterRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PostDAO {

	List<PostRecord> findAll();

	List<PostRecord> findAll(Pagination pagination);

	List<PostRecord> findAllBySource(SourceRecord source, Pagination pagination);

	List<PostRecord> findAllByYearMonth(Integer year, Integer month);

	List<PostRecord> findAllByLabel(LabelRecord label, Pagination pagination);
	
	List<PostRecord> findAllByShared(boolean shared);
	
	List<PostRecord> findNewsletter();

	PostRecord findByURL(String url);

	PostRecord findByHash(String hash);

	Long countAll();

	Long countBy(SourceRecord source);

	Long countBy(LabelRecord label);
	
	Long countBy(NewsletterRecord newsletter);

	Long countAuthors();

	List<Map<String, Object>> getArchiveByDates();

	List<Map<String, Object>> getArchiveByLabels();

	List<Map<String, Object>> getArchiveBySources();
	
	List<Map<String, Object>> getArchiveByNewsletter(NewsletterRecord newsletter);
	
	int updateNewsletter(Collection<PostRecord> posts, NewsletterRecord newslettter);
}