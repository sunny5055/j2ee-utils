package com.googlecode.jutils.jsf.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.googlecode.jutils.jsf.scope.CustomScope;
import com.googlecode.jutils.jsf.util.FacesUtils;

/**
 * The listener interface for receiving customScope events. The class that is
 * interested in processing a customScope event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addCustomScopeListener<code> method. When
 * the customScope event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see CustomScopeEvent
 */
@SuppressWarnings("serial")
public class CustomScopeListener implements PhaseListener {

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
            if (FacesUtils.getSessionAttribute(facesContext, FacesUtils.CUSTOM_SCOPE) == null) {
                FacesUtils.setSessionAttribute(facesContext, FacesUtils.CUSTOM_SCOPE, new CustomScope());
            }
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void afterPhase(PhaseEvent event) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        if (event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
            final CustomScope flashScope = (CustomScope) FacesUtils.getSessionAttribute(facesContext,
                    FacesUtils.CUSTOM_SCOPE);
            if (flashScope != null) {
                flashScope.clear();
            }
        }
    }
}
