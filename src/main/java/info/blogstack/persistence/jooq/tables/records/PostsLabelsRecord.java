/**
 * This class is generated by jOOQ
 */
package info.blogstack.persistence.jooq.tables.records;


import info.blogstack.persistence.jooq.tables.PostsLabels;
import info.blogstack.persistence.jooq.tables.interfaces.IPostsLabels;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.1"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PostsLabelsRecord extends UpdatableRecordImpl<PostsLabelsRecord> implements Record3<Long, Long, Long>, IPostsLabels {

	private static final long serialVersionUID = 1260832878;

	/**
	 * Setter for <code>BLOGSTACK.POSTS_LABELS.ID</code>.
	 */
	@Override
	public void setId(Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POSTS_LABELS.ID</code>.
	 */
	@Override
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>BLOGSTACK.POSTS_LABELS.POST_ID</code>.
	 */
	@Override
	public void setPostId(Long value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POSTS_LABELS.POST_ID</code>.
	 */
	@Override
	public Long getPostId() {
		return (Long) getValue(1);
	}

	/**
	 * Setter for <code>BLOGSTACK.POSTS_LABELS.LABEL_ID</code>.
	 */
	@Override
	public void setLabelId(Long value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POSTS_LABELS.LABEL_ID</code>.
	 */
	@Override
	public Long getLabelId() {
		return (Long) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Long> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Long, Long, Long> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Long, Long, Long> valuesRow() {
		return (Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return PostsLabels.POSTS_LABELS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field2() {
		return PostsLabels.POSTS_LABELS.POST_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field3() {
		return PostsLabels.POSTS_LABELS.LABEL_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value2() {
		return getPostId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value3() {
		return getLabelId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostsLabelsRecord value1(Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostsLabelsRecord value2(Long value) {
		setPostId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostsLabelsRecord value3(Long value) {
		setLabelId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostsLabelsRecord values(Long value1, Long value2, Long value3) {
		value1(value1);
		value2(value2);
		value3(value3);
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IPostsLabels from) {
		setId(from.getId());
		setPostId(from.getPostId());
		setLabelId(from.getLabelId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IPostsLabels> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached PostsLabelsRecord
	 */
	public PostsLabelsRecord() {
		super(PostsLabels.POSTS_LABELS);
	}

	/**
	 * Create a detached, initialised PostsLabelsRecord
	 */
	public PostsLabelsRecord(Long id, Long postId, Long labelId) {
		super(PostsLabels.POSTS_LABELS);

		setValue(0, id);
		setValue(1, postId);
		setValue(2, labelId);
	}
}