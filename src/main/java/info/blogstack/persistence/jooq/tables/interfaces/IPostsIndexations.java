/**
 * This class is generated by jOOQ
 */
package info.blogstack.persistence.jooq.tables.interfaces;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.4" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface IPostsIndexations extends java.io.Serializable {

	/**
	 * Setter for <code>BLOGSTACK.POSTS_INDEXATIONS.ID</code>.
	 */
	public void setId(java.lang.Long value);

	/**
	 * Getter for <code>BLOGSTACK.POSTS_INDEXATIONS.ID</code>.
	 */
	public java.lang.Long getId();

	/**
	 * Setter for <code>BLOGSTACK.POSTS_INDEXATIONS.POST_ID</code>.
	 */
	public void setPostId(java.lang.Long value);

	/**
	 * Getter for <code>BLOGSTACK.POSTS_INDEXATIONS.POST_ID</code>.
	 */
	public java.lang.Long getPostId();

	/**
	 * Setter for <code>BLOGSTACK.POSTS_INDEXATIONS.INDEXATION_ID</code>.
	 */
	public void setIndexationId(java.lang.Long value);

	/**
	 * Getter for <code>BLOGSTACK.POSTS_INDEXATIONS.INDEXATION_ID</code>.
	 */
	public java.lang.Long getIndexationId();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IPostsIndexations
	 */
	public void from(info.blogstack.persistence.jooq.tables.interfaces.IPostsIndexations from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IPostsIndexations
	 */
	public <E extends info.blogstack.persistence.jooq.tables.interfaces.IPostsIndexations> E into(E into);
}
