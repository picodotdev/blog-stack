package info.blogstack.components;

import info.blogstack.entities.Adsense;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class Ads {

	@Parameter
	@Property
	private Adsense adsense;

	@Environmental
	private JavaScriptSupport support;

	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("blogstack", toJSON(Adsense.BLOGSTACK));

		if (adsense != null) {
			spec.put("ad", toJSON(adsense));
		}

		support.require("app/adsensem").invoke("init").with(spec);
	}

	private JSONObject toJSON(Adsense adsense) {
		JSONObject json = new JSONObject();
		json.put("adClient", adsense.getAdsenseAdClient());
		json.put("adSlotHorizontalSkycraper", adsense.getAdsenseSlotHorizontalSkycraper());
		json.put("adSlotBigRectangle", adsense.getAdsenseSlotBigRectangle());
		json.put("adSlotWideSkycraper", adsense.getAdsenseSlotWideSkycraper());
		return json;
	}
}