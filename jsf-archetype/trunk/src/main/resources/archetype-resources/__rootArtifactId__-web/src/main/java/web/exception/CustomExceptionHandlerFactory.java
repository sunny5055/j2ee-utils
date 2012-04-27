#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Classe pour créer des objets qui gèrent les exceptions
 */
public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {
    private ExceptionHandlerFactory parent;

    /**
     * Instantie un nouveau custom exception handler factory.
     * 
     * @param parent le parent
     */
    public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    /*
     * (non-Javadoc)
     * @see javax.faces.context.ExceptionHandlerFactory${symbol_pound}getExceptionHandler()
     */
    /**
     * Gére les exceptions
     */
    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler result = parent.getExceptionHandler();
        result = new CustomExceptionHandler(result);

        return result;
    }
}
