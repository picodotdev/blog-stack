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

		var ad1 = (p1 == 0) ? this.spec.blogstack : this.spec.ad;
		var ad2 = (p2 == 0) ? this.spec.blogstack : this.spec.ad;
		var ad3 = (p3 == 0) ? this.spec.blogstack : this.spec.ad;

		var adsense = templates.adsense();
		var adBillboard = adsense({dimensions: "width:970px;height:250px;", width: 970, height: 250, adClient: ad1.adClient, adSlot: ad1.adSlot });
		var adHorizontalSkycraper = adsense({dimensions: "width:728px;height:90px;", width: 728, height: 90, adClient: ad1.adClient, adSlot: ad1.adSlot });
		var adBigRectangle = adsense({dimensions: "width:336px;height:280px;", width: 336, height: 280, adClient: ad2.adClient, adSlot: ad2.adSlot });
		var adWideSkycraper = adsense({dimensions: "width:160px;height:600px;", width: 160, height: 600, adClient: ad3.adClient, adSlot: ad3.adSlot });
		var adBigSkycraper = adsense({dimensions: "width:300px;height:600px;", width: 300, height: 600, adClient: ad3.adClient, adSlot: ad3.adSlot });

		alert(adBigSkycraper);
		
		try {
			// Load the ad blocks with a delay, in Firefox they add a browser history entry (only in the archive)
			setTimeout(function() { $("#billboard").html(adBillboard); }, 250);
			setTimeout(function() { $("#horizontalSkycraper").html(adHorizontalSkycraper); }, 250);
			setTimeout(function() { $("#bigRectangle").html(adBigRectangle); }, 500);
			setTimeout(function() { $("#wideSkycraper").html(adWideSkycraper); }, 750);
			setTimeout(function() { $("#bigSkycraper").html(adBigSkycraper); }, 750);
		} catch (exception) {
			console.error(exception);
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