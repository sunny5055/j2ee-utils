package com.google.code.jee.utils.jsf.util;

import org.primefaces.context.RequestContext;

import com.google.code.jee.utils.StringUtil;

/**
 * The Class RequestContextUtil.
 */
public final class RequestContextUtil {

    /**
     * Instantiates a new request context util.
     */
    private RequestContextUtil() {
        super();
    }

    /**
     * Adds the callback param.
     * 
     * @param name the name
     * @param value the value
     */
    public static final void addCallbackParam(String name, Object value) {
        final RequestContext requestContext = RequestContext.getCurrentInstance();
        if (requestContext != null) {
            requestContext.addCallbackParam(name, value);
        }
    }

    /**
     * Adds the class.
     * 
     * @param componentId the component id
     * @param cssClass the css class
     */
    public static final void addClass(String componentId, String cssClass) {
        if (!StringUtil.isBlank(componentId) && !StringUtil.isBlank(cssClass)) {
            execute(componentId, "addClass(\"" + cssClass + "\")");
        }
    }

    /**
     * Removes the class.
     * 
     * @param componentId the component id
     * @param cssClass the css class
     */
    public static final void removeClass(String componentId, String cssClass) {
        if (!StringUtil.isBlank(componentId) && !StringUtil.isBlank(cssClass)) {
            execute(componentId, "removeClass(\"" + cssClass + "\")");
        }
    }

    /**
     * Execute.
     * 
     * @param jsExpression the js expression
     */
    public static final void execute(String jsExpression) {
        if (!StringUtil.isBlank(jsExpression)) {
            final RequestContext requestContext = RequestContext.getCurrentInstance();

            if (requestContext != null) {
                requestContext.execute(jsExpression);
            }
        }
    }

    /**
     * Execute.
     * 
     * @param componentId the component id
     * @param jsExpression the js expression
     */
    public static final void execute(String componentId, String jsExpression) {
        if (!StringUtil.isBlank(componentId) && !StringUtil.isBlank(jsExpression)) {
            final String clientId = escapeClientId(componentId);
            if (!StringUtil.isBlank(clientId)) {
                final String fullJsExpression = "$(\"" + clientId + "\")." + jsExpression + ";";

                execute(fullJsExpression);
            }
        }
    }

    /**
     * Adds the partial update target.
     * 
     * @param componentId the component id
     */
    public static final void update(String componentId) {
        final RequestContext requestContext = RequestContext.getCurrentInstance();
        if (requestContext != null) {
            requestContext.update(componentId);
        }
    }

    /**
     * Escape client id.
     * 
     * @param componentId the component id
     * @return the string
     */
    private static String escapeClientId(String componentId) {
        String clientId = null;
        if (!StringUtil.isBlank(componentId)) {
            clientId = StringUtil.replace(componentId, ":", "\\\\:");
        }
        return clientId;
    }
}
