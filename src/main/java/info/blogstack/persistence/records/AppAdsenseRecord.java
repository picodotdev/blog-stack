package info.blogstack.persistence.records;

import info.blogstack.persistence.jooq.tables.records.AdsenseRecord;

public class AppAdsenseRecord extends AdsenseRecord {

	private static final long serialVersionUID = -6944119544715881571L;

	public static final AdsenseRecord BLOGSTACK;
	
	static {
		BLOGSTACK = new AppAdsenseRecord();
		BLOGSTACK.setAdsenseadclient("ca-pub-3533636310991304");
		BLOGSTACK.setAdsenseslotbigrectangle("2873752587");
		BLOGSTACK.setAdsenseslotwideskycraper("1896546988");
		BLOGSTACK.setAdsenseslothorizontalskycraper("1397019380");
	}
}