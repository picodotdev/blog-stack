/**
 * This class is generated by jOOQ
 */
package info.blogstack.persistence.jooq.tables.records;


import info.blogstack.persistence.jooq.tables.Indexation;
import info.blogstack.persistence.jooq.tables.interfaces.IIndexation;

import javax.annotation.Generated;

import org.joda.time.DateTime;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
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
public class IndexationRecord extends UpdatableRecordImpl<IndexationRecord> implements Record2<Long, DateTime>, IIndexation {

	private static final long serialVersionUID = 882478841;

	/**
	 * Setter for <code>BLOGSTACK.INDEXATION.ID</code>.
	 */
	@Override
	public void setId(Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.INDEXATION.ID</code>.
	 */
	@Override
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>BLOGSTACK.INDEXATION.CREATIONDATE</code>.
	 */
	@Override
	public void setCreationdate(DateTime value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>BLOGSTACK.INDEXATION.CREATIONDATE</code>.
	 */
	@Override
	public DateTime getCreationdate() {
		return (DateTime) getValue(1);
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
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Long, DateTime> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Long, DateTime> valuesRow() {
		return (Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return Indexation.INDEXATION.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<DateTime> field2() {
		return Indexation.INDEXATION.CREATIONDATE;
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
	public IndexationRecord value1(Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IndexationRecord value2(DateTime value) {
		setCreationdate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IndexationRecord values(Long value1, DateTime value2) {
		value1(value1);
		value2(value2);
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IIndexation from) {
		setId(from.getId());
		setCreationdate(from.getCreationdate());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IIndexation> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached IndexationRecord
	 */
	public IndexationRecord() {
		super(Indexation.INDEXATION);
	}

	/**
	 * Create a detached, initialised IndexationRecord
	 */
	public IndexationRecord(Long id, DateTime creationdate) {
		super(Indexation.INDEXATION);

		setValue(0, id);
		setValue(1, creationdate);
	}
}