#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.bean.datatable.filters;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

@SuppressWarnings("serial")
public abstract class AbstractFiltersBean implements Serializable {

	@PostConstruct
	protected void init() {
	}

	public abstract boolean hasFilters();

	protected abstract void reInit();

	public abstract Map<String, String> getFilters();

	public abstract void clearFilters(ActionEvent e);
}
