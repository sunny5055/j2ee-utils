#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.exception;

import javax.faces.context.ExceptionHandler;
import com.google.code.jee.utils.jsf.exception.AbstractExceptionHandler;

/**
 * The Class CustomExceptionHandler.
 */
public class CustomExceptionHandler extends AbstractExceptionHandler {

    /**
     * Instantiates a new custom exception handler.
     * 
     * @param wrapped the wrapped
     */
    public CustomExceptionHandler(ExceptionHandler wrapped) {
        super(wrapped);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public String getViewExpiredPage() {
        return "/xhtml/index.xhtml";
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public String getExceptionTitle() {
        return "error_exception_title";
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public String getExceptionContent() {
        return "error_exception_content";
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public String getErrorPage() {
        return "/xhtml/error.xhtml";
    }
}

