define("app/lastPosts", ["require", "jquery", "app/templates"], function(require, $, templates) {
	function LastPosts(spec) {
		var _this = this;
		this.spec = spec;

		var source = require(["app/sources/" + this.spec.source], function(source) {
			var lastPosts = templates.lastPosts();
		
			var html = lastPosts(source);
			$('#' + _this.spec.id).html(html);
		});
	}
	
	function init(spec) {
		new LastPosts(spec);
	}

	return {
		init: init
	}
});