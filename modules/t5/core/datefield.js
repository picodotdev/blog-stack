(function() {
  define(["./dom", "./events", "./messages", "./ajax", "underscore", "./fields"], function(dom, events, messages, ajax, _) {
    var Controller, activePopup, datePickerFirstDay, days, scan, serverFirstDay;

    serverFirstDay = parseInt(messages("date-symbols.first-day"));
    datePickerFirstDay = serverFirstDay === 0 ? 6 : serverFirstDay - 1;
    DatePicker.months = (messages("date-symbols.months")).split(",");
    days = (messages("date-symbols.days")).split(",");
    days.push(days.shift());
    DatePicker.days = _.map(days, function(name) {
      return name.substr(0, 1).toLowerCase();
    });
    DatePicker.TODAY = messages("core-datefield-today");
    DatePicker.NONE = messages("core-datefield-none");
    activePopup = null;
    Controller = (function() {
      function Controller(container) {
        var _this = this;

        this.container = container;
        this.field = this.container.findFirst("input");
        this.trigger = this.container.findFirst("button");
        this.trigger.on("click", function() {
          _this.doTogglePopup();
          return false;
        });
      }

      Controller.prototype.showPopup = function() {
        if (activePopup && activePopup !== this.popup) {
          activePopup.hide();
        }
        this.popup.show();
        return activePopup = this.popup;
      };

      Controller.prototype.hidePopup = function() {
        this.popup.hide();
        return activePopup = null;
      };

      Controller.prototype.doTogglePopup = function() {
        var value,
          _this = this;

        if (this.field.element.disabled) {
          return;
        }
        if (!this.popup) {
          this.createPopup();
          if (activePopup != null) {
            activePopup.hide();
          }
        } else if (this.popup.visible()) {
          this.hidePopup();
          return;
        }
        value = this.field.value();
        if (value === "") {
          this.datePicker.setDate(null);
          this.showPopup();
          return;
        }
        this.field.addClass("ajax-wait");
        return ajax(this.container.attr("data-parse-url"), {
          data: {
            input: value
          },
          onerror: function(message) {
            _this.field.removeClass("ajax-wait");
            return _this.fieldError(message);
          },
          success: function(response) {
            var date, reply;

            _this.field.removeClass("ajax-wait");
            reply = response.json;
            if (reply.result) {
              _this.clearFieldError();
              date = new Date();
              date.setTime(reply.result);
              _this.datePicker.setDate(date);
              _this.showPopup();
              return;
            }
            _this.fieldError(_.escape(reply.error));
            _this.hidePopup();
          }
        });
      };

      Controller.prototype.fieldError = function(message) {
        return this.field.focus().trigger(events.field.showValidationError, {
          message: message
        });
      };

      Controller.prototype.clearFieldError = function() {
        return this.field.trigger(events.field.clearValidationError);
      };

      Controller.prototype.createPopup = function() {
        this.datePicker = new DatePicker();
        this.datePicker.setFirstWeekDay(datePickerFirstDay);
        this.popup = dom.create("div", {
          "class": "datefield-popup well"
        }).append(this.datePicker.create());
        this.container.insertAfter(this.popup);
        return this.datePicker.onselect = _.bind(this.onSelect, this);
      };

      Controller.prototype.onSelect = function() {
        var date,
          _this = this;

        date = this.datePicker.getDate();
        if (date === null) {
          this.hidePopup();
          this.clearFieldError();
          this.field.value("");
          return;
        }
        this.field.addClass("ajax-wait");
        return ajax(this.container.attr("data-format-url"), {
          data: {
            input: date.getTime()
          },
          failure: function(response, message) {
            _this.field.removeClass("ajax-wait");
            return _this.fieldError(message);
          },
          success: function(response) {
            _this.field.removeClass("ajax-wait");
            _this.clearFieldError();
            _this.field.value(response.json.result);
            return _this.hidePopup();
          }
        });
      };

      return Controller;

    })();
    scan = function(root) {
      var container, _i, _len, _ref, _results;

      _ref = root.find("[data-component-type='core/DateField']");
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        container = _ref[_i];
        container.attr("data-component-type", null);
        _results.push(new Controller(container));
      }
      return _results;
    };
    scan(dom.body);
    dom.onDocument(events.zone.didUpdate, function() {
      return scan(this);
    });
    return null;
  });

}).call(this);
