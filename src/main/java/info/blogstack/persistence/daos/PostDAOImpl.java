package info.blogstack.persistence.daos;

import static info.blogstack.persistence.jooq.Tables.LABEL;
import static info.blogstack.persistence.jooq.Tables.POST;
import static info.blogstack.persistence.jooq.Tables.POSTS_LABELS;
import static info.blogstack.persistence.jooq.Tables.SOURCE;
import info.blogstack.persistence.jooq.tables.Post;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.NewsletterRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public class PostDAOImpl implements PostDAO {

	private DSLContext context;

	public PostDAOImpl(DSLContext context) {
		this.context = context;
	}

	@Override
	public List<PostRecord> findAll() {
		return context.selectFrom(POST).where(POST.VISIBLE.isTrue()).fetch();
	}

	@Override
	public List<PostRecord> findAll(Pagination pagination) {
		return context.selectFrom(POST).where(POST.VISIBLE.isTrue()).orderBy(pagination.getFields()).limit(pagination.getOffset(), pagination.getNumberOfRows()).fetch();
	}

	@Override
	public List<PostRecord> findAllBySource(SourceRecord source, Pagination pagination) {
		return context.selectFrom(POST).where(POST.VISIBLE.isTrue().and(POST.SOURCE_ID.eq(source.getId()))).orderBy(pagination.getFields())
				.limit(pagination.getOffset(), pagination.getNumberOfRows()).fetch();
	}

	@Override
	public List<PostRecord> findAllByYearMonth(Integer year, Integer month) {
		Post p = POST.as("p");
		return context.selectFrom(p).where(p.VISIBLE.isTrue().and("year(\"p\".publishDate) = ?", year).and("month(\"p\".publishDate) = ?", month)).orderBy(p.DATE.desc())
				.fetch();
	}

	@Override
	public List<PostRecord> findAllByLabel(LabelRecord label, Pagination pagination) {
		return context.select().from(POST).join(POSTS_LABELS).on(POST.ID.eq(POSTS_LABELS.POST_ID)).where(POST.VISIBLE.isTrue().and(POSTS_LABELS.LABEL_ID.in(label.getId())))
				.orderBy(pagination.getFields()).limit(pagination.getOffset(), pagination.getNumberOfRows()).fetchInto(POST);
	}
	
	@Override
	public List<PostRecord> findAllByShared(boolean shared) {
		return context.select().from(POST).where(POST.SHARED.eq(shared)).orderBy(POST.CREATIONDATE.asc()).fetchInto(POST);
	}
	
	@Override
	public List<PostRecord> findNewsletter() {
		DateTime friday = new DateTime().withDayOfWeek(5).withHourOfDay(7);
		DateTime tuesday = new DateTime().withDayOfWeek(2).withHourOfDay(7);
		DateTime date = null;

		if (friday.isBeforeNow()) {
		    date = friday;
		} else if (tuesday.isBeforeNow()) {
			date = tuesday;
		}

		if (date == null) {
			return Collections.EMPTY_LIST;
		}
		
		return context.select().from(POST).where(POST.NEWSLETTER_ID.isNull()).and(POST.CREATIONDATE.lt(date)).orderBy(POST.CREATIONDATE.asc()).fetchInto(POST);
	}

	@Override
	public PostRecord findByURL(String url) {
		return context.selectFrom(POST).where(POST.URL.eq(url)).fetchOne();
	}

	@Override
	public PostRecord findByHash(String hash) {
		return context.selectFrom(POST).where(POST.HASH.eq(hash)).fetchOne();
	}

	@Override
	public Long countAll() {
		return context.selectCount().from(POST).where(POST.VISIBLE.isTrue()).fetchOne(0, Long.class);
	}

	@Override
	public Long countBy(SourceRecord source) {
		return context.selectCount().from(POST).where(POST.VISIBLE.isTrue().and(POST.SOURCE_ID.eq(source.getId()))).fetchOne(0, Long.class);
	}

	@Override
	public Long countBy(LabelRecord label) {
		return context.selectCount().from(POST).join(POSTS_LABELS).on(POST.ID.eq(POSTS_LABELS.POST_ID))
				.where(POST.VISIBLE.isTrue().and(POSTS_LABELS.LABEL_ID.in(label.getId()))).fetchOne(0, Long.class);
	}
	
	@Override
	public Long countBy(NewsletterRecord newsletter) {
		return context.selectCount().from(POST).where(POST.NEWSLETTER_ID.eq(newsletter.getId())).fetchOne(0, Long.class);
	}

	@Override
	public Long countAuthors() {
		return context.select(DSL.countDistinct(DSL.concat(POST.SOURCE_ID, POST.AUTHOR))).from(POST).fetchOne(0, Long.class);
	}

	@Override
	public List<Map<String, Object>> getArchiveByDates() {
		return context
				.fetch("select year(p.publishDate) as year, month(p.publishDate) as month, count(*) as posts from BLOGSTACK.POST as p where p.visible = true group by year(p.publishDate), month(p.publishDate) order by year(p.publishDate) desc, month(p.publishDate) desc")
				.intoMaps();
	}

	@Override
	public List<Map<String, Object>> getArchiveByLabels() {
		List<Map<String, Object>> d = context
				.fetch("select l.id as id, count(*) as posts from BLOGSTACK.LABEL as l inner join BLOGSTACK.POSTS_LABELS as pl on l.id = pl.label_id inner join BLOGSTACK.POST as p on pl.post_id = p.id where l.enabled = true and p.visible = true group by l.id order by l.name")
				.intoMaps();
		for (Map<String, Object> m : d) {
			LabelRecord label = context.selectFrom(LABEL).where(LABEL.ID.eq((Long) m.get("ID"))).fetchOne();
			m.put("LABEL", label);
		}
		return d;
	}

	@Override
	public List<Map<String, Object>> getArchiveBySources() {
		List<Map<String, Object>> d = context
				.fetch("select s.id as id, count(*) as posts from BLOGSTACK.SOURCE as s inner join BLOGSTACK.POST as p on s.id = p.source_id where p.visible = true group by s.id order by s.name")
				.intoMaps();
		for (Map<String, Object> m : d) {
			SourceRecord source = context.selectFrom(SOURCE).where(SOURCE.ID.eq((Long) m.get("ID"))).fetchOne();
			m.put("SOURCE", source);
		}
		return d;
	}
	
	@Override
	public List<Map<String, Object>> getArchiveByNewsletter(NewsletterRecord newsletter) {
		List<Map<String, Object>> d = context
				.fetch("select l.id as id, count(*) as posts from BLOGSTACK.LABEL as l inner join BLOGSTACK.POSTS_LABELS as pl on l.id = pl.label_id inner join BLOGSTACK.POST as p on pl.post_id = p.id where l.enabled = true and p.visible = true and p.newsletter_id = ? group by l.id order by l.name", newsletter.getId())
				.intoMaps();
		for (Map<String, Object> m : d) {
			LabelRecord label = context.selectFrom(LABEL).where(LABEL.ID.eq((Long) m.get("ID"))).fetchOne();
			m.put("LABEL", label);
		}
		return d;
	}
	
	@Override
	public int updateNewsletter(Collection<PostRecord> posts, NewsletterRecord newslettter) {
		Collection<Long> ids = new ArrayList<>();
		for (PostRecord post : posts) {
			ids.add(post.getId());
		} 
		return context.update(POST).set(POST.NEWSLETTER_ID, newslettter.getId()).where(POST.ID.in(ids)).execute();
	}
}