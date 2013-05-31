#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.exception;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ${package}.web.bean.ErrorBean;
import ${package}.web.util.FacesUtils;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator();
        while (it.hasNext()) {
            final ExceptionQueuedEvent event = it.next();
            final ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            final Throwable exception = context.getException();

            FacesUtils.setSessionAttribute("exception", exception);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(exception.getMessage(), exception);
            }

            try {
                if (exception instanceof ViewExpiredException) {
                    FacesUtils.redirect("/xhtml/index.xhtml", true);
                } else {

                    final ErrorBean errorBean = FacesUtils.getBean("errorBean", ErrorBean.class);
                    if (errorBean != null) {
                        final String title = FacesUtils.getLabel("error_exception_title");
                        errorBean.setTitle(title);

                        final String message = FacesUtils.getLabel("error_exception_content");
                        errorBean.setMessage(message);
                    }

                    FacesUtils.redirect("/xhtml/error.xhtml", false);
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
}
