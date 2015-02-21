define("app/background", ["jquery"], function($) {

	function random(min, max) {
	    return Math.floor(Math.random() * (max - min + 1) + min);
	}
	
	function init(spec) {
		var i = random(0, spec.backgrounds.length - 1);
		var b = spec.backgrounds[i];
		$('body').css('background-image', 'url(/assets/images/backgrounds/' + b + ')');
	}

	return {
		init: init
	}
});