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
public interface ILabel extends java.io.Serializable {

	/**
	 * Setter for <code>BLOGSTACK.LABEL.ID</code>.
	 */
	public void setId(java.lang.Long value);

	/**
	 * Getter for <code>BLOGSTACK.LABEL.ID</code>.
	 */
	public java.lang.Long getId();

	/**
	 * Setter for <code>BLOGSTACK.LABEL.CREATIONDATE</code>.
	 */
	public void setCreationdate(org.joda.time.DateTime value);

	/**
	 * Getter for <code>BLOGSTACK.LABEL.CREATIONDATE</code>.
	 */
	public org.joda.time.DateTime getCreationdate();

	/**
	 * Setter for <code>BLOGSTACK.LABEL.NAME</code>.
	 */
	public void setName(java.lang.String value);

	/**
	 * Getter for <code>BLOGSTACK.LABEL.NAME</code>.
	 */
	public java.lang.String getName();

	/**
	 * Setter for <code>BLOGSTACK.LABEL.HASH</code>.
	 */
	public void setHash(java.lang.String value);

	/**
	 * Getter for <code>BLOGSTACK.LABEL.HASH</code>.
	 */
	public java.lang.String getHash();

	/**
	 * Setter for <code>BLOGSTACK.LABEL.ENABLED</code>.
	 */
	public void setEnabled(java.lang.Boolean value);

	/**
	 * Getter for <code>BLOGSTACK.LABEL.ENABLED</code>.
	 */
	public java.lang.Boolean getEnabled();

	/**
	 * Setter for <code>BLOGSTACK.LABEL.VISIBLE</code>.
	 */
	public void setVisible(java.lang.Boolean value);

	/**
	 * Getter for <code>BLOGSTACK.LABEL.VISIBLE</code>.
	 */
	public java.lang.Boolean getVisible();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ILabel
	 */
	public void from(info.blogstack.persistence.jooq.tables.interfaces.ILabel from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ILabel
	 */
	public <E extends info.blogstack.persistence.jooq.tables.interfaces.ILabel> E into(E into);
}
