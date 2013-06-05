#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.listener;

import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import ${package}.web.bean.LocaleBean;
import ${package}.web.util.FacesUtils;

@SuppressWarnings("serial")
public class LocalePhaseListener implements PhaseListener {

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			final LocaleBean localeBean = (LocaleBean) FacesUtils.getSessionAttribute("localeBean");
			Locale locale = null;
			if (localeBean != null) {
				locale = localeBean.getLocale();
			} else {
				locale = facesContext.getViewRoot().getLocale();
			}

			if (locale != null) {
				FacesUtils.updateLocale(locale);
			}
		}
	}

	@Override
	public void afterPhase(PhaseEvent event) {
	}

}
