/**
 * This class is generated by jOOQ
 */
package info.blogstack.persistence.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.4" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PostsIndexations extends org.jooq.impl.TableImpl<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord> {

	private static final long serialVersionUID = 263733503;

	/**
	 * The singleton instance of <code>BLOGSTACK.POSTS_INDEXATIONS</code>
	 */
	public static final info.blogstack.persistence.jooq.tables.PostsIndexations POSTS_INDEXATIONS = new info.blogstack.persistence.jooq.tables.PostsIndexations();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord> getRecordType() {
		return info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord.class;
	}

	/**
	 * The column <code>BLOGSTACK.POSTS_INDEXATIONS.ID</code>.
	 */
	public final org.jooq.TableField<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord, java.lang.Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>BLOGSTACK.POSTS_INDEXATIONS.POST_ID</code>.
	 */
	public final org.jooq.TableField<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord, java.lang.Long> POST_ID = createField("POST_ID", org.jooq.impl.SQLDataType.BIGINT, this, "");

	/**
	 * The column <code>BLOGSTACK.POSTS_INDEXATIONS.INDEXATION_ID</code>.
	 */
	public final org.jooq.TableField<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord, java.lang.Long> INDEXATION_ID = createField("INDEXATION_ID", org.jooq.impl.SQLDataType.BIGINT, this, "");

	/**
	 * Create a <code>BLOGSTACK.POSTS_INDEXATIONS</code> table reference
	 */
	public PostsIndexations() {
		this("POSTS_INDEXATIONS", null);
	}

	/**
	 * Create an aliased <code>BLOGSTACK.POSTS_INDEXATIONS</code> table reference
	 */
	public PostsIndexations(java.lang.String alias) {
		this(alias, info.blogstack.persistence.jooq.tables.PostsIndexations.POSTS_INDEXATIONS);
	}

	private PostsIndexations(java.lang.String alias, org.jooq.Table<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord> aliased) {
		this(alias, aliased, null);
	}

	private PostsIndexations(java.lang.String alias, org.jooq.Table<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, info.blogstack.persistence.jooq.Blogstack.BLOGSTACK, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord, java.lang.Long> getIdentity() {
		return info.blogstack.persistence.jooq.Keys.IDENTITY_POSTS_INDEXATIONS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord> getPrimaryKey() {
		return info.blogstack.persistence.jooq.Keys.PK_POSTS_INDEXATIONS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord>>asList(info.blogstack.persistence.jooq.Keys.PK_POSTS_INDEXATIONS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord, ?>>asList(info.blogstack.persistence.jooq.Keys.POSTS_INDEXATIONS_POST_ID, info.blogstack.persistence.jooq.Keys.POSTS_INDEXATIONS_INDEXATION_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public info.blogstack.persistence.jooq.tables.PostsIndexations as(java.lang.String alias) {
		return new info.blogstack.persistence.jooq.tables.PostsIndexations(alias, this);
	}

	/**
	 * Rename this table
	 */
	public info.blogstack.persistence.jooq.tables.PostsIndexations rename(java.lang.String name) {
		return new info.blogstack.persistence.jooq.tables.PostsIndexations(name, null);
	}
}
