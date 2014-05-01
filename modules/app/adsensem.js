// The name inf firefox must be not [ads, adsense] 
define("app/adsensem", ["jquery", "app/templates"], function($, templates) {	
	function Adsense(spec) {
		this.spec = spec;

		// 3 ad blocks
		// 1 block for blogstack, 1 form the source y the last random
		// the type of ad is random
		
		var p1 = 0;
		var p2 = 0;
		var p3 = 0;
		if (this.spec.ad != null) {
			p1 = random(0, 1);
			p2 = random(0, 1);
			p3 = (p1 == p2) ? p1 : ramdom(0, 1);
			p3 = (p3 == 0) ? 1 : 0;
		}

		var horizontalSkycraper = (p1 == 0) ? this.spec.blogstack : this.spec.ad;
		var bigRectangle = (p2 == 0) ? this.spec.blogstack : this.spec.ad;
		var wideSkycraper = (p3 == 0) ? this.spec.blogstack : this.spec.ad;

		var adsense = templates.adsense();
		var adHorizontalSkycraper = adsense({dimensions:"width:728px;height:90px;", adClient: horizontalSkycraper.adClient, adSlot: horizontalSkycraper.adSlotHorizontalSkycraper });
		var adBigRectangle = adsense({dimensions:"width:336px;height:280px;", adClient: bigRectangle.adClient, adSlot: bigRectangle.adSlotBigRectangle });
		var adWideSkycraper = adsense({dimensions:"width:160px;height:600px;", adClient: wideSkycraper.adClient, adSlot: wideSkycraper.adSlotWideSkycraper });

		try {
			$("#horizontalSkycraper").html(adHorizontalSkycraper);
			$("#bigRectangle").html(adBigRectangle);
			$("#wideSkycraper").html(adWideSkycraper);
		} catch (exception) {
			console.error(exception)
		}
	}
	
	function random(min, max) {
	    return Math.floor(Math.random() * (max - min + 1) + min);
	}

	function init(spec) {
		new Adsense(spec);
	}

	return {
		init: init
	}
});