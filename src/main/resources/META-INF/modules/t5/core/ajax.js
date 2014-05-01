(function() {
  define(["./pageinit", "./dom", "./exception-frame", "./console", "underscore"], function(pageinit, dom, exceptionframe, console, _) {
    return function(url, options) {
      var newOptions;

      newOptions = _.extend({}, options, {
        exception: function(exception) {
          console.error("Request to " + url + " failed with " + exception);
          if (options.exception) {
            return options.exception(exception);
          } else {
            throw exception;
          }
        },
        failure: function(response, failureMessage) {
          var contentType, isHTML, message, raw;

          raw = response.header("X-Tapestry-ErrorMessage");
          if (!_.isEmpty(raw)) {
            message = window.unescape(raw);
            console.error("Request to " + url + " failed with '" + message + "'.");
            contentType = response.header("content-type");
            isHTML = contentType && (contentType.split(';')[0] === "text/html");
            if (isHTML) {
              exceptionframe(response.text);
            }
          } else {
            console.error(failureMessage);
          }
          options.failure && options.failure(response);
          return null;
        },
        success: function(response) {
          return pageinit.handlePartialPageRenderResponse(response, options.success);
        }
      });
      return dom.ajaxRequest(url, newOptions);
    };
  });

}).call(this);
