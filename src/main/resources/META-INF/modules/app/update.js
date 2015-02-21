define("app/update", ["jquery", "moment",  "app/lastUpdated"], function($, moment, lastUpdated) {
	function Update(spec) {
		this.spec = spec;

		var hace = moment(lastUpdated.date, "YYYYMMDDHHmmZ").lang("es").startOf("second").from(moment.utc());
		var fecha = moment(lastUpdated.date, "YYYYMMDDHHmmZ").lang("es").utc().format("dddd[, ]DD[ de ]MMMM[ de ]YYYY[ a las ]HH:mm UTC");
		
		$('#' + this.spec.id).html("Último artículo indexado " + hace + ", " + fecha);
	}
	
	function init(spec) {
		return new Update(spec);
	}
	
	return {
		init: init
	}
});