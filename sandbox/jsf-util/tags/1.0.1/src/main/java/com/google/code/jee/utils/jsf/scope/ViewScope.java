package com.google.code.jee.utils.jsf.scope;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * The Class ViewScope.
 */
public class ViewScope implements Scope {

    /**
     * {@inheritedDoc}
     */
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        final Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
        if (viewMap.containsKey(name)) {
            return viewMap.get(name);
        } else {
            final Object object = objectFactory.getObject();
            viewMap.put(name, object);
            return object;
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Object remove(String name) {
        return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public String getConversationId() {
        return null;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        // Not supported
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }
}
