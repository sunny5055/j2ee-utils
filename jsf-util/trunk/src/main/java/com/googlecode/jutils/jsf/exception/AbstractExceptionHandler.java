package com.googlecode.jutils.jsf.exception;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.log4j.Logger;

import com.googlecode.jutils.jsf.bean.ErrorBean;
import com.googlecode.jutils.jsf.util.FacesUtils;

/**
 * The Class CustomExceptionHandler.
 */
public abstract class AbstractExceptionHandler extends ExceptionHandlerWrapper {
    private static final Logger LOGGER = Logger.getLogger(AbstractExceptionHandler.class);
    protected ExceptionHandler wrapped;

    /**
     * Instantiates a new custom exception handler.
     * 
     * @param wrapped the wrapped
     */
    public AbstractExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Gets the wrapped.
     * 
     * @return the wrapped {@inheritedDoc}
     */
    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    /**
     * Handle.
     * 
     * @throws FacesException the faces exception {@inheritedDoc}
     */
    @Override
    public void handle() throws FacesException {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator();
        while (it.hasNext()) {
            final ExceptionQueuedEvent event = it.next();
            final ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            final Throwable exception = context.getException();

            FacesUtils.setSessionAttribute(facesContext, "exception", exception);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(exception.getMessage(), exception);
            }

            try {
                if (exception instanceof ViewExpiredException) {
                    FacesUtils.redirect(facesContext, getViewExpiredPage(), true);
                } else {

                    final ErrorBean errorBean = FacesUtils.getBean(facesContext, "errorBean", ErrorBean.class);
                    if (errorBean != null) {
                        errorBean.setTitle(getExceptionTitle());
                        errorBean.setMessage(getExceptionContent());
                    }

                    FacesUtils.redirect(facesContext, getErrorPage(), false);
                }
            } catch (final IOException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(e.getMessage(), e);
                }
            } finally {
                it.remove();
            }
        }

        getWrapped().handle();
    }

    /**
     * Gets the view expired page.
     * 
     * @return the view expired page
     */
    public abstract String getViewExpiredPage();

    /**
     * Gets the exception title.
     * 
     * @return the exception title
     */
    public abstract String getExceptionTitle();

    /**
     * Gets the exception content.
     * 
     * @return the exception content
     */
    public abstract String getExceptionContent();

    /**
     * Gets the error page.
     * 
     * @return the error page
     */
    public abstract String getErrorPage();
}
