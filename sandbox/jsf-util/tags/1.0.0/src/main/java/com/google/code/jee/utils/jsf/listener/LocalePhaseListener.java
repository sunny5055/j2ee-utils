package com.google.code.jee.utils.jsf.listener;

import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.google.code.jee.utils.jsf.bean.LocaleBean;
import com.google.code.jee.utils.jsf.util.FacesUtils;

/**
 * The listener interface for receiving localePhase events. The class that is
 * interested in processing a localePhase event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addLocalePhaseListener</code> method. When the localePhase
 * event occurs, that object's appropriate method is invoked.
 * 
 * @see LocalePhaseEvent
 */
@SuppressWarnings("serial")
public class LocalePhaseListener implements PhaseListener {

    /**
     * {@inheritedDoc}
     */
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void beforePhase(PhaseEvent event) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            final LocaleBean localeBean = (LocaleBean) FacesUtils.getSessionAttribute(facesContext, "localeBean");
            Locale locale = null;
            if (localeBean != null) {
                locale = localeBean.getLocale();
            } else {
                locale = facesContext.getViewRoot().getLocale();
            }

            if (locale != null) {
                FacesUtils.updateLocale(facesContext, locale);
            }
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void afterPhase(PhaseEvent event) {
    }

}
