(function() {
  define(["moment"], function(moment) {
    var locale;

    locale = (document.documentElement.getAttribute("data-locale")) || "en";
    moment.lang(locale);
    return moment;
  });

}).call(this);
