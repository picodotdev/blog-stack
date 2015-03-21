package info.blogstack.persistence.daos;

import static info.blogstack.persistence.jooq.Tables.LABEL;
import static info.blogstack.persistence.jooq.Tables.POSTS_LABELS;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;

public class LabelDAOImpl implements LabelDAO {

	private DSLContext context;

	public LabelDAOImpl(DSLContext context) {
		this.context = context;
	}

	@Override
	public List<LabelRecord> findAll() {
		return context.selectFrom(LABEL).where(LABEL.ENABLED.isTrue()).fetchInto(LabelRecord.class);
	}

	@Override
	public List<LabelRecord> findAll(Pagination pagination) {
		return context.selectFrom(LABEL).where(LABEL.ENABLED.isTrue()).limit(pagination.getOffset(), pagination.getNumberOfRows()).fetchInto(LabelRecord.class);
	}

	@Override
	public LabelRecord findByName(String name) {
		return context.selectFrom(LABEL).where(LABEL.NAME.eq(name)).fetchOne();
	}

	@Override
	public LabelRecord findByHash(String hash) {
		return context.selectFrom(LABEL).where(LABEL.HASH.eq(hash)).fetchOne();
	}

	@Override
	public List<LabelRecord> findByPost(PostRecord post, int n) {
		return findByPost(post, n, true);
	}

	@Override
	public List<LabelRecord> findByPost(PostRecord post, int n, boolean visible) {
		List<Map<String, Object>> labels = context.select(LABEL.ID).from(LABEL).join(POSTS_LABELS).on(LABEL.ID.eq(POSTS_LABELS.LABEL_ID))
				.where(POSTS_LABELS.POST_ID.eq(post.getId()).and(LABEL.ENABLED.isTrue())).and(LABEL.VISIBLE.eq(visible)).fetchMaps();
		List<Long> ids = new ArrayList<>();
		for (Map<String, Object> label : labels) {
			ids.add((Long) label.get("ID"));
		}
		return context.select(LABEL.fields()).from(LABEL).join(POSTS_LABELS).on(LABEL.ID.eq(POSTS_LABELS.LABEL_ID)).where(LABEL.ID.in(ids)).groupBy(LABEL.ID)
				.orderBy(POSTS_LABELS.POST_ID.count().desc()).limit(n).fetchInto(LabelRecord.class);
	}

	@Override
	public Long countAll() {
		return context.selectCount().from(LABEL).where(LABEL.ENABLED.isTrue()).fetchOne(0, Long.class);
	}
}