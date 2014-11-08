package info.blogstack.persistence.daos;

import org.jooq.SortField;

public class Pagination {

	private int offset;
	private int numberOfRows;
	
	private SortField<?>[] fields;
	
	public Pagination(int offset, int numberOfRows, SortField<?>... fields) {
		this.offset = offset;
		this.numberOfRows = numberOfRows;
		this.fields = fields;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public SortField<?>[] getFields() {
		return fields;
	}

	public void setFields(SortField<?>[] fields) {
		this.fields = fields;
	}	
}