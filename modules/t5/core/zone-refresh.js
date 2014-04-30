(function() {
  define(["./events", "./dom", "./console"], function(events, dom, console) {
    var initialize;

    initialize = function(zoneId, period, url) {
      var executing, handler, intervalId, zone;

      zone = dom(zoneId);
      if (!zone) {
        console.err("Zone " + zoneId + " not found for periodic refresh.");
        return;
      }
      if (zone.meta("periodic-refresh")) {
        return;
      }
      zone.meta("periodic-refresh", true);
      executing = false;
      zone.on(events.zone.didUpdate, function() {
        return executing = false;
      });
      handler = function() {
        if (executing) {
          return;
        }
        executing = true;
        return zone.trigger(events.zone.refresh, {
          url: url
        });
      };
      intervalId = window.setInterval(handler, period * 1000);
      return (dom(window)).on("beforeunload", function() {
        return window.clearInterval(intervalId);
      });
    };
    return initialize;
  });

}).call(this);
