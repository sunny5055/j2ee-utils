package com.googlecode.jutils.jsf.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * The listener interface for receiving session events. The class that is
 * interested in processing a session event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addSessionListener<code> method. When
 * the session event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see SessionEvent
 */
public class SessionListener implements HttpSessionListener {
    private static Integer SESSIONS_COUNT = 0;

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        SESSIONS_COUNT++;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        if (SESSIONS_COUNT > 0) {
            SESSIONS_COUNT--;
        }
    }

    /**
     * Gets the active session.
     * 
     * @return the active session
     */
    public static Integer getActiveSession() {
        return SESSIONS_COUNT;
    }
}
