define("app/karmacracy", ["jquery", "app/templates"], function($, templates) {
	function Karmacracy(spec) {
		this.spec = spec;

		var karmacracy = templates.karmacracy();
		var widget = karmacracy({id:this.spec.id, url: this.spec.url });

		$(this.spec.selector).replaceWith(widget);
	}
	
	function init(spec) {
		new Karmacracy(spec);
	}

	return {
		init: init
	}
});