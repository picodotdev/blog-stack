package info.blogstack.persistence.records;

import info.blogstack.misc.Utils;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;

public class AppLabelRecord extends LabelRecord {

	private static final long serialVersionUID = -2554705695211026321L;
	
	public void updateHash() {		
		setHash(Utils.getHash(this));
	}
}