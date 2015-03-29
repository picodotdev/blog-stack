/**
 * This class is generated by jOOQ
 */
package info.blogstack.persistence.jooq.tables.interfaces;

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
public interface INewsletter extends java.io.Serializable {

	/**
	 * Setter for <code>BLOGSTACK.NEWSLETTER.ID</code>.
	 */
	public void setId(java.lang.Long value);

	/**
	 * Getter for <code>BLOGSTACK.NEWSLETTER.ID</code>.
	 */
	public java.lang.Long getId();

	/**
	 * Setter for <code>BLOGSTACK.NEWSLETTER.CREATIONDATE</code>.
	 */
	public void setCreationdate(org.joda.time.DateTime value);

	/**
	 * Getter for <code>BLOGSTACK.NEWSLETTER.CREATIONDATE</code>.
	 */
	public org.joda.time.DateTime getCreationdate();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface INewsletter
	 */
	public void from(info.blogstack.persistence.jooq.tables.interfaces.INewsletter from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface INewsletter
	 */
	public <E extends info.blogstack.persistence.jooq.tables.interfaces.INewsletter> E into(E into);
}
