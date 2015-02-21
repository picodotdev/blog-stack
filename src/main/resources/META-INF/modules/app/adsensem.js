// The name in firefox must be not [ads, adsense] 
define("app/adsensem", ["jquery", "app/templates"], function($, templates) {	
	function Adsense(spec) {
		this.spec = spec;

		// 4 ad blocks
		// 2 random blocks for blogstack, 2 random blocks for the source
		// the type of ad is random
		
		var p1 = 0;
		var p2 = 0;
		var p3 = 0;
		var p4 = 0;
		if (this.spec.ad != null) {
			p1 = random(0, 1);
			p2 = random(0, 1);
			if (p1 == p2) {
				if (p1 == 0) {
					p3 = 1;
					p4 = 1;
				} else {
					p3 = 0;
					p4 = 0;
				}
			} else {
				p3 = random(0, 1);
				p4 = (p1 == p3) ? p2 : p1;
			}
		}

		var ad1 = (p1 == 0) ? this.spec.blogstack : this.spec.ad;
		var ad2 = (p2 == 0) ? this.spec.blogstack : this.spec.ad;
		var ad3 = (p3 == 0) ? this.spec.blogstack : this.spec.ad;
		var ad4 = (p4 == 0) ? this.spec.blogstack : this.spec.ad;

		var adsense = templates.adsense();
		var adLeaderboard1 = adsense({dimensions: "width:728px;height:90px;", width: 728, height: 90, adClient: ad1.adClient, adSlot: ad1.adSlot });
		var adBillboard1 = adsense({dimensions: "width:970px;height:250px;", width: 970, height: 250, adClient: ad2.adClient, adSlot: ad2.adSlot });
		var adBigRectangle1 = adsense({dimensions: "width:336px;height:280px;", width: 336, height: 280, adClient: ad3.adClient, adSlot: ad3.adSlot });
		var adBigRectangle2 = adsense({dimensions: "width:336px;height:280px;", width: 336, height: 280, adClient: ad4.adClient, adSlot: ad4.adSlot });

		try {
			// Load the ad blocks with a delay, in Firefox they add a browser history entry (only in the archive)
			setTimeout(function() { $("#adLeaderboard1").html(adLeaderboard1); }, 250);
			setTimeout(function() { $("#adBillboard1").html(adBillboard1); }, 500);
			setTimeout(function() { $("#adBigRectangle1").html(adBigRectangle1); }, 750);
			setTimeout(function() { $("#adBigRectangle2").html(adBigRectangle2); }, 1000);
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