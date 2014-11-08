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
public interface ISource extends java.io.Serializable {

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.ID</code>.
	 */
	public void setId(java.lang.Long value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.ID</code>.
	 */
	public java.lang.Long getId();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.CREATIONDATE</code>.
	 */
	public void setCreationdate(org.joda.time.DateTime value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.CREATIONDATE</code>.
	 */
	public org.joda.time.DateTime getCreationdate();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.UPDATEDATE</code>.
	 */
	public void setUpdatedate(org.joda.time.DateTime value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.UPDATEDATE</code>.
	 */
	public org.joda.time.DateTime getUpdatedate();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.NAME</code>.
	 */
	public void setName(java.lang.String value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.NAME</code>.
	 */
	public java.lang.String getName();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.ALIAS</code>.
	 */
	public void setAlias(java.lang.String value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.ALIAS</code>.
	 */
	public java.lang.String getAlias();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.PAGEURL</code>.
	 */
	public void setPageurl(java.lang.String value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.PAGEURL</code>.
	 */
	public java.lang.String getPageurl();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.URL</code>.
	 */
	public void setUrl(java.lang.String value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.URL</code>.
	 */
	public java.lang.String getUrl();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.AUTHOR</code>.
	 */
	public void setAuthor(java.lang.String value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.AUTHOR</code>.
	 */
	public java.lang.String getAuthor();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.EMAIL</code>.
	 */
	public void setEmail(java.lang.String value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.EMAIL</code>.
	 */
	public java.lang.String getEmail();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.DISQUSSHORTNAME</code>.
	 */
	public void setDisqusshortname(java.lang.String value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.DISQUSSHORTNAME</code>.
	 */
	public java.lang.String getDisqusshortname();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.ADSENSE_ID</code>.
	 */
	public void setAdsenseId(java.lang.Long value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.ADSENSE_ID</code>.
	 */
	public java.lang.Long getAdsenseId();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.IMPORTSOURCE_ID</code>.
	 */
	public void setImportsourceId(java.lang.Long value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.IMPORTSOURCE_ID</code>.
	 */
	public java.lang.Long getImportsourceId();

	/**
	 * Setter for <code>BLOGSTACK.SOURCE.ENABLED</code>.
	 */
	public void setEnabled(java.lang.Boolean value);

	/**
	 * Getter for <code>BLOGSTACK.SOURCE.ENABLED</code>.
	 */
	public java.lang.Boolean getEnabled();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ISource
	 */
	public void from(info.blogstack.persistence.jooq.tables.interfaces.ISource from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ISource
	 */
	public <E extends info.blogstack.persistence.jooq.tables.interfaces.ISource> E into(E into);
}
