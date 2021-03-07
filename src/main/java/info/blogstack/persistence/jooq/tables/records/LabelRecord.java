/**
 * This class is generated by jOOQ
 */
package info.blogstack.persistence.jooq.tables.records;


import info.blogstack.persistence.jooq.tables.Label;
import info.blogstack.persistence.jooq.tables.interfaces.ILabel;

import javax.annotation.Generated;

import org.joda.time.DateTime;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
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
public class LabelRecord extends UpdatableRecordImpl<LabelRecord> implements Record6<Long, DateTime, String, String, Boolean, Boolean>, ILabel {

	private static final long serialVersionUID = 1889155489;

	/**
	 * Setter for <code>BLOGSTACK.LABEL.ID</code>.
	 */
	@Override
	public void setId(Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.LABEL.ID</code>.
	 */
	@Override
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>BLOGSTACK.LABEL.CREATIONDATE</code>.
	 */
	@Override
	public void setCreationdate(DateTime value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.LABEL.CREATIONDATE</code>.
	 */
	@Override
	public DateTime getCreationdate() {
		return (DateTime) getValue(1);
	}

	/**
	 * Setter for <code>BLOGSTACK.LABEL.NAME</code>.
	 */
	@Override
	public void setName(String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.LABEL.NAME</code>.
	 */
	@Override
	public String getName() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>BLOGSTACK.LABEL.HASH</code>.
	 */
	@Override
	public void setHash(String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.LABEL.HASH</code>.
	 */
	@Override
	public String getHash() {
		return (String) getValue(3);
	}

	/**
	 * Setter for <code>BLOGSTACK.LABEL.ENABLED</code>.
	 */
	@Override
	public void setEnabled(Boolean value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.LABEL.ENABLED</code>.
	 */
	@Override
	public Boolean getEnabled() {
		return (Boolean) getValue(4);
	}

	/**
	 * Setter for <code>BLOGSTACK.LABEL.VISIBLE</code>.
	 */
	@Override
	public void setVisible(Boolean value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.LABEL.VISIBLE</code>.
	 */
	@Override
	public Boolean getVisible() {
		return (Boolean) getValue(5);
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
	// Record6 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row6<Long, DateTime, String, String, Boolean, Boolean> fieldsRow() {
		return (Row6) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row6<Long, DateTime, String, String, Boolean, Boolean> valuesRow() {
		return (Row6) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return Label.LABEL.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<DateTime> field2() {
		return Label.LABEL.CREATIONDATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return Label.LABEL.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field4() {
		return Label.LABEL.HASH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Boolean> field5() {
		return Label.LABEL.ENABLED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Boolean> field6() {
		return Label.LABEL.VISIBLE;
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
	public DateTime value2() {
		return getCreationdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value4() {
		return getHash();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean value5() {
		return getEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean value6() {
		return getVisible();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LabelRecord value1(Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LabelRecord value2(DateTime value) {
		setCreationdate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LabelRecord value3(String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LabelRecord value4(String value) {
		setHash(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LabelRecord value5(Boolean value) {
		setEnabled(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LabelRecord value6(Boolean value) {
		setVisible(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LabelRecord values(Long value1, DateTime value2, String value3, String value4, Boolean value5, Boolean value6) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(ILabel from) {
		setId(from.getId());
		setCreationdate(from.getCreationdate());
		setName(from.getName());
		setHash(from.getHash());
		setEnabled(from.getEnabled());
		setVisible(from.getVisible());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends ILabel> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached LabelRecord
	 */
	public LabelRecord() {
		super(Label.LABEL);
	}

	/**
	 * Create a detached, initialised LabelRecord
	 */
	public LabelRecord(Long id, DateTime creationdate, String name, String hash, Boolean enabled, Boolean visible) {
		super(Label.LABEL);

		setValue(0, id);
		setValue(1, creationdate);
		setValue(2, name);
		setValue(3, hash);
		setValue(4, enabled);
		setValue(5, visible);
	}
}