PrimeFaces.widget.DataTable.prototype.unselectRow = function(r, event) {
    var row = this.findRow(r),
    rowMeta = this.getRowMeta(row);
    if(this.isMultipleSelection() && event && !event.metaKey) {
        this.selectRow(row, event);
    } else {
        //remove visual style
        row.removeClass('ui-state-highlight');

        //remove from selection
        this.removeSelection(rowMeta.key);

        //save state
        this.writeSelections();

        this.fireRowUnselectEvent(rowMeta.key);
    }
}

/**************************************************************************************************************/

PrimeFaces.widget.DataTable.prototype.reloadFilters = function() {
	if (this.isSelectionEnabled()) {
		this.clearSelection();
	}

	var options = {
		source : this.id,
		update : this.id,
		process : this.id,
		formId : this.cfg.formId
	};

	var _self = this;

	options.onsuccess = function(responseXML) {
		var xmlDoc = $(responseXML.documentElement);
		var extension = xmlDoc.find("extension");
		if(extension && extension.text().length > 0) {
			var data = $.parseJSON(extension.text());
			if(data) {
				if (data.filterValues) {
					filterValues = $.parseJSON(data.filterValues);
					for ( var key in filterValues) {
						if (filterValues.hasOwnProperty(key)) {
							var id = PrimeFaces.escapeClientId(_self.id + ':'
									+ key + '_filter');
							if($(id).length <= 0) {
								id = PrimeFaces.escapeClientId(key);
							}
							if($(id).length > 0) {
								var tagName = $(id)[0].tagName.toLowerCase();
								if (tagName == 'input') {
									$(id).val(filterValues[key]);
								} else if (tagName == 'select') {
									$(id + " option").each(function() {
										this.selected = equalsIgnoreCase(this.value, filterValues[key]);
									});
								}
							}
						}
					}
				}

				if (data.selectedRowIds) {
					var selectedRowIds = $.parseJSON(data.selectedRowIds);
					var rows = [];

					for(var i=0; i < selectedRowIds.length; i++) {
						var selectedRowId = selectedRowIds[i];
						// save state
						_self.addSelection(selectedRowId);
					}

					var availableRows = $("tr[data-rk]", _self.jqId);
					for(var i=0; i < availableRows.length; i++) {
						var availableRow = availableRows[i];
						var rowId = $(availableRow).attr("data-rk");
						if($.inArray(rowId, selectedRowIds) > -1) {
							rows.push(availableRow);
						}
					}

					if(rows.length > 0) {
						var checkboxColumn = _self.cfg.columnSelectionMode && _self.cfg.columnSelectionMode == 'multiple';
				    	updateRowSelection(rows, checkboxColumn, 100)();
					}

					_self.writeSelections();
				    _self.fireRowSelectEvent(null);
				}
			}
		}

        return true;
	};

	var params = {};
	params[this.id + "_reloadFilters"] = true;
	options.params = params;
	PrimeFaces.ajax.AjaxRequest(options);
};

PrimeFaces.widget.DataTable.prototype.reloadContent = function() {
	if (this.isSelectionEnabled()) {
		this.clearSelection();
	}

	var options = {
		source : this.id,
		update : this.id,
		process : this.id,
		formId : this.cfg.formId
	};

	var _self = this;
	options.onsuccess = function(responseXML) {
		var xmlDoc = $(responseXML.documentElement);

		var extension = xmlDoc.find("extension");
		if(extension && extension.text().length > 0) {
			var data = $.parseJSON(extension.text());
			if(data) {
				if (data.filterValues) {
					filterValues = $.parseJSON(data.filterValues);
					for ( var key in filterValues) {
						if (filterValues.hasOwnProperty(key)) {
							var id = PrimeFaces.escapeClientId(_self.id + ':'
									+ key + '_filter');
							if($(id).length <= 0) {
								id = PrimeFaces.escapeClientId(key);
							}
							if($(id).length > 0) {
								var tagName = $(id)[0].tagName.toLowerCase();
								if (tagName == 'input') {
									$(id).val(filterValues[key]);
								} else if (tagName == 'select') {
									$(id + " option").each(function() {
										this.selected = equalsIgnoreCase(this.value, filterValues[key]);
									});
								}
							}
						}
					}
				}
			}
		}

		var updates = xmlDoc.find("update");
        for(var i=0; i < updates.length; i++) {
        	var update = updates.eq(i),
            id = update.attr('id'),
            content = update.text();

            if(id == _self.id){
                $(_self.tbody).replaceWith(content);

                if(_self.cfg.scrollable||_self.cfg.resizableColumns) {
                    _self.updateDataCellWidths();
                }
            }
            else {
                PrimeFaces.ajax.AjaxUtils.updateElement.call(this, id, content);
            }
        }

        PrimeFaces.ajax.AjaxUtils.handleResponse.call(this, xmlDoc);


		return true;
	};

	var params = {};
	params[this.id + "_reloadContent"] = true;

	options.params = params;
	PrimeFaces.ajax.AjaxRequest(options);
};

PrimeFaces.widget.DataTable.prototype.unload = function() {
	var options = {
		source : this.id,
		update : this.id,
		process : this.id,
		formId : this.cfg.formId
	};

	var params = {};
	params[this.id + "_unload"] = true;

	options.params = params;
	PrimeFaces.ajax.AjaxRequest(options);
};

function equalsIgnoreCase(value1, value2) {
	return value1.toLowerCase() == value2.toLowerCase();
}

function updateRowSelection(rows, checkboxColumn, chunksize) {
  var current = 0;
  return function() {
    // changes n elements in a loop
    for (var i=0; i<rows.length && i<chunksize; i++) {
		var row = $(rows[i]);
		if(!checkboxColumn) {
			row.removeClass('ui-state-hover').addClass('ui-state-highlight');
	    }else {
			var checkbox = $('.ui-chkbox-box',row);
			checkbox.addClass('ui-state-active').children('span.ui-chkbox-icon:first').addClass('ui-icon ui-icon-check');
			row.addClass('ui-state-highlight').attr('aria-selected', true);
	    }
    }
    current += chunksize;
    // calls itself again after a short break
    if (current < rows.length) setTimeout(arguments.callee, 10);
  };
}