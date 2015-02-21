/**
 * This class is generated by jOOQ
 */
package info.blogstack.persistence.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PostRecord extends org.jooq.impl.UpdatableRecordImpl<info.blogstack.persistence.jooq.tables.records.PostRecord> implements org.jooq.Record14<java.lang.Long, org.joda.time.DateTime, org.joda.time.DateTime, org.joda.time.DateTime, org.joda.time.DateTime, java.lang.String, java.lang.String, java.lang.String, java.lang.String, byte[], java.lang.Long, java.lang.Long, java.lang.Boolean, java.lang.Boolean>, info.blogstack.persistence.jooq.tables.interfaces.IPost {

	private static final long serialVersionUID = 1557151491;

	/**
	 * Setter for <code>BLOGSTACK.POST.ID</code>.
	 */
	@Override
	public void setId(java.lang.Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.ID</code>.
	 */
	@Override
	public java.lang.Long getId() {
		return (java.lang.Long) getValue(0);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.CREATIONDATE</code>.
	 */
	@Override
	public void setCreationdate(org.joda.time.DateTime value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.CREATIONDATE</code>.
	 */
	@Override
	public org.joda.time.DateTime getCreationdate() {
		return (org.joda.time.DateTime) getValue(1);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.UPDATEDATE</code>.
	 */
	@Override
	public void setUpdatedate(org.joda.time.DateTime value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.UPDATEDATE</code>.
	 */
	@Override
	public org.joda.time.DateTime getUpdatedate() {
		return (org.joda.time.DateTime) getValue(2);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.PUBLISHDATE</code>.
	 */
	@Override
	public void setPublishdate(org.joda.time.DateTime value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.PUBLISHDATE</code>.
	 */
	@Override
	public org.joda.time.DateTime getPublishdate() {
		return (org.joda.time.DateTime) getValue(3);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.DATE</code>.
	 */
	@Override
	public void setDate(org.joda.time.DateTime value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.DATE</code>.
	 */
	@Override
	public org.joda.time.DateTime getDate() {
		return (org.joda.time.DateTime) getValue(4);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.URL</code>.
	 */
	@Override
	public void setUrl(java.lang.String value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.URL</code>.
	 */
	@Override
	public java.lang.String getUrl() {
		return (java.lang.String) getValue(5);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.HASH</code>.
	 */
	@Override
	public void setHash(java.lang.String value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.HASH</code>.
	 */
	@Override
	public java.lang.String getHash() {
		return (java.lang.String) getValue(6);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.TITLE</code>.
	 */
	@Override
	public void setTitle(java.lang.String value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.TITLE</code>.
	 */
	@Override
	public java.lang.String getTitle() {
		return (java.lang.String) getValue(7);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.AUTHOR</code>.
	 */
	@Override
	public void setAuthor(java.lang.String value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.AUTHOR</code>.
	 */
	@Override
	public java.lang.String getAuthor() {
		return (java.lang.String) getValue(8);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.CONTENTCOMPRESSED</code>.
	 */
	@Override
	public void setContentcompressed(byte[] value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.CONTENTCOMPRESSED</code>.
	 */
	@Override
	public byte[] getContentcompressed() {
		return (byte[]) getValue(9);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.INDEXATION_ID</code>.
	 */
	@Override
	public void setIndexationId(java.lang.Long value) {
		setValue(10, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.INDEXATION_ID</code>.
	 */
	@Override
	public java.lang.Long getIndexationId() {
		return (java.lang.Long) getValue(10);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.SOURCE_ID</code>.
	 */
	@Override
	public void setSourceId(java.lang.Long value) {
		setValue(11, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.SOURCE_ID</code>.
	 */
	@Override
	public java.lang.Long getSourceId() {
		return (java.lang.Long) getValue(11);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.VISIBLE</code>.
	 */
	@Override
	public void setVisible(java.lang.Boolean value) {
		setValue(12, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.VISIBLE</code>.
	 */
	@Override
	public java.lang.Boolean getVisible() {
		return (java.lang.Boolean) getValue(12);
	}

	/**
	 * Setter for <code>BLOGSTACK.POST.SHARED</code>.
	 */
	@Override
	public void setShared(java.lang.Boolean value) {
		setValue(13, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.POST.SHARED</code>.
	 */
	@Override
	public java.lang.Boolean getShared() {
		return (java.lang.Boolean) getValue(13);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Long> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record14 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row14<java.lang.Long, org.joda.time.DateTime, org.joda.time.DateTime, org.joda.time.DateTime, org.joda.time.DateTime, java.lang.String, java.lang.String, java.lang.String, java.lang.String, byte[], java.lang.Long, java.lang.Long, java.lang.Boolean, java.lang.Boolean> fieldsRow() {
		return (org.jooq.Row14) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row14<java.lang.Long, org.joda.time.DateTime, org.joda.time.DateTime, org.joda.time.DateTime, org.joda.time.DateTime, java.lang.String, java.lang.String, java.lang.String, java.lang.String, byte[], java.lang.Long, java.lang.Long, java.lang.Boolean, java.lang.Boolean> valuesRow() {
		return (org.jooq.Row14) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field1() {
		return info.blogstack.persistence.jooq.tables.Post.POST.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.joda.time.DateTime> field2() {
		return info.blogstack.persistence.jooq.tables.Post.POST.CREATIONDATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.joda.time.DateTime> field3() {
		return info.blogstack.persistence.jooq.tables.Post.POST.UPDATEDATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.joda.time.DateTime> field4() {
		return info.blogstack.persistence.jooq.tables.Post.POST.PUBLISHDATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.joda.time.DateTime> field5() {
		return info.blogstack.persistence.jooq.tables.Post.POST.DATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field6() {
		return info.blogstack.persistence.jooq.tables.Post.POST.URL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field7() {
		return info.blogstack.persistence.jooq.tables.Post.POST.HASH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field8() {
		return info.blogstack.persistence.jooq.tables.Post.POST.TITLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field9() {
		return info.blogstack.persistence.jooq.tables.Post.POST.AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<byte[]> field10() {
		return info.blogstack.persistence.jooq.tables.Post.POST.CONTENTCOMPRESSED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field11() {
		return info.blogstack.persistence.jooq.tables.Post.POST.INDEXATION_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field12() {
		return info.blogstack.persistence.jooq.tables.Post.POST.SOURCE_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Boolean> field13() {
		return info.blogstack.persistence.jooq.tables.Post.POST.VISIBLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Boolean> field14() {
		return info.blogstack.persistence.jooq.tables.Post.POST.SHARED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.joda.time.DateTime value2() {
		return getCreationdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.joda.time.DateTime value3() {
		return getUpdatedate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.joda.time.DateTime value4() {
		return getPublishdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.joda.time.DateTime value5() {
		return getDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value6() {
		return getUrl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value7() {
		return getHash();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value8() {
		return getTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value9() {
		return getAuthor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] value10() {
		return getContentcompressed();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value11() {
		return getIndexationId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value12() {
		return getSourceId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Boolean value13() {
		return getVisible();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Boolean value14() {
		return getShared();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value1(java.lang.Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value2(org.joda.time.DateTime value) {
		setCreationdate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value3(org.joda.time.DateTime value) {
		setUpdatedate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value4(org.joda.time.DateTime value) {
		setPublishdate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value5(org.joda.time.DateTime value) {
		setDate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value6(java.lang.String value) {
		setUrl(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value7(java.lang.String value) {
		setHash(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value8(java.lang.String value) {
		setTitle(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value9(java.lang.String value) {
		setAuthor(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value10(byte[] value) {
		setContentcompressed(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value11(java.lang.Long value) {
		setIndexationId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value12(java.lang.Long value) {
		setSourceId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value13(java.lang.Boolean value) {
		setVisible(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord value14(java.lang.Boolean value) {
		setShared(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostRecord values(java.lang.Long value1, org.joda.time.DateTime value2, org.joda.time.DateTime value3, org.joda.time.DateTime value4, org.joda.time.DateTime value5, java.lang.String value6, java.lang.String value7, java.lang.String value8, java.lang.String value9, byte[] value10, java.lang.Long value11, java.lang.Long value12, java.lang.Boolean value13, java.lang.Boolean value14) {
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(info.blogstack.persistence.jooq.tables.interfaces.IPost from) {
		setId(from.getId());
		setCreationdate(from.getCreationdate());
		setUpdatedate(from.getUpdatedate());
		setPublishdate(from.getPublishdate());
		setDate(from.getDate());
		setUrl(from.getUrl());
		setHash(from.getHash());
		setTitle(from.getTitle());
		setAuthor(from.getAuthor());
		setContentcompressed(from.getContentcompressed());
		setIndexationId(from.getIndexationId());
		setSourceId(from.getSourceId());
		setVisible(from.getVisible());
		setShared(from.getShared());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends info.blogstack.persistence.jooq.tables.interfaces.IPost> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached PostRecord
	 */
	public PostRecord() {
		super(info.blogstack.persistence.jooq.tables.Post.POST);
	}

	/**
	 * Create a detached, initialised PostRecord
	 */
	public PostRecord(java.lang.Long id, org.joda.time.DateTime creationdate, org.joda.time.DateTime updatedate, org.joda.time.DateTime publishdate, org.joda.time.DateTime date, java.lang.String url, java.lang.String hash, java.lang.String title, java.lang.String author, byte[] contentcompressed, java.lang.Long indexationId, java.lang.Long sourceId, java.lang.Boolean visible, java.lang.Boolean shared) {
		super(info.blogstack.persistence.jooq.tables.Post.POST);

		setValue(0, id);
		setValue(1, creationdate);
		setValue(2, updatedate);
		setValue(3, publishdate);
		setValue(4, date);
		setValue(5, url);
		setValue(6, hash);
		setValue(7, title);
		setValue(8, author);
		setValue(9, contentcompressed);
		setValue(10, indexationId);
		setValue(11, sourceId);
		setValue(12, visible);
		setValue(13, shared);
	}
}
