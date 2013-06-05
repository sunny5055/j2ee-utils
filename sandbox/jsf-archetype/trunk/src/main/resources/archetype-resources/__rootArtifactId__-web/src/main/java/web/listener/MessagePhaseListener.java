#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.googlecode.jutils.collection.CollectionUtil;

import ${package}.web.util.FacesUtils;

@SuppressWarnings("serial")
public class MessagePhaseListener implements PhaseListener {

    /**
     * {@inheritedDoc}
     */
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void beforePhase(PhaseEvent event) {
        if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
            final FacesContext facesContext = event.getFacesContext();
            if (!facesContext.getResponseComplete()) {
                restoreMessages(facesContext);
            }
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void afterPhase(PhaseEvent event) {
        if (!PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
            final FacesContext facesContext = event.getFacesContext();
            saveMessages(facesContext);
        }

    }

    @SuppressWarnings("unchecked")
    private void saveMessages(FacesContext facesContext) {
        final List<FacesMessage> messages = new ArrayList<FacesMessage>();
        for (final Iterator<FacesMessage> it = facesContext.getMessages(null); it.hasNext();) {
            messages.add(it.next());
            it.remove();
        }

        if (!CollectionUtil.isEmpty(messages)) {
            final List<FacesMessage> existingMessages = (List<FacesMessage>) FacesUtils.getSessionAttribute(FacesUtils.MSGS_KEY);
            if (!CollectionUtil.isEmpty(existingMessages)) {
                existingMessages.addAll(messages);
            } else {
                FacesUtils.setSessionAttribute(FacesUtils.MSGS_KEY, messages);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void restoreMessages(FacesContext facesContext) {
        final List<FacesMessage> messages = (List<FacesMessage>) FacesUtils.getSessionAttribute(FacesUtils.MSGS_KEY);
        FacesUtils.removeSessionAttribute(FacesUtils.MSGS_KEY);

        if (!CollectionUtil.isEmpty(messages)) {
            for (final FacesMessage message : messages) {
                facesContext.addMessage(null, message);
            }
        }
    }
}