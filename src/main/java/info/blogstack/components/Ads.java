package info.blogstack.components;

import info.blogstack.persistence.jooq.tables.records.AdsenseRecord;
import info.blogstack.persistence.records.AppAdsenseRecord;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class Ads {

	@Parameter
	@Property
	private AdsenseRecord adsense;

	@Environmental
	private JavaScriptSupport support;

	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("blogstack", toJSON(AppAdsenseRecord.BLOGSTACK));

		if (adsense != null) {
			spec.put("ad", toJSON(adsense));
		}

		support.require("app/adsensem").invoke("init").with(spec);
	}

	private JSONObject toJSON(AdsenseRecord adsense) {
		JSONObject json = new JSONObject();
		json.put("adClient", adsense.getAdsenseadclient());
		json.put("adSlot", adsense.getAdsenseslothorizontalskycraper());
		return json;
	}
}