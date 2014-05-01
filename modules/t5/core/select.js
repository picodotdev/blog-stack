(function() {
  define(["./events", "./dom", "./zone"], function(events, dom, zone) {
    return dom.onDocument("change", "select[data-update-zone]", function() {
      var containingZone;

      containingZone = zone.findZone(this);
      return containingZone && containingZone.trigger(events.zone.refresh, {
        url: this.attr("data-update-url"),
        parameters: {
          "t:selectvalue": this.value()
        }
      });
    });
  });

}).call(this);
