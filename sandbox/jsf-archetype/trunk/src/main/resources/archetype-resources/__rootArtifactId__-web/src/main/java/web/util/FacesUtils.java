#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.dom4j.Dom4jXPath;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;

@SuppressWarnings("unchecked")
public final class FacesUtils {
    public static final String MSGS_KEY = "FACES_MESSAGES";
    public static final String HAS_ERROR_MESSAGE = "hasErrorMessage";
    public static final String HAS_WARN_MESSAGE = "hasWarnMessage";
    public static final String HAS_INFO_MESSAGE = "hasInfoMessage";
    private static final Logger LOGGER = Logger.getLogger(FacesUtils.class);

    private static final String CONFIG_FILES_KEY = "javax.faces.CONFIG_FILES";
    private static final String DEFAULT_CONFIG_FILES = "/WEB-INF/faces-config.xml";
    private static String BUNDLE_NAME;

    private FacesUtils() {
    }

    public static FacesMessage getMessage(FacesContext facesContext, FacesMessage.Severity severity, String msg) {
        final String label = getLabel(facesContext, msg);
        final FacesMessage facesMessage = new FacesMessage(severity, label, label);
        return facesMessage;
    }

    public static void addMessageIntoSession(FacesContext facesContext, FacesMessage.Severity severity, String msg) {
        final FacesMessage facesMessage = getMessage(facesContext, severity, msg);
        if (facesMessage != null) {
            final List<FacesMessage> existingMessages = (List<FacesMessage>) FacesUtils.getSessionAttribute(
                    facesContext, FacesUtils.MSGS_KEY);
            if (!CollectionUtil.isEmpty(existingMessages)) {
                existingMessages.add(facesMessage);
            } else {
                FacesUtils.setSessionAttribute(facesContext, FacesUtils.MSGS_KEY, Arrays.asList(facesMessage));
            }
        }
    }

    public static void addInfoMessage(FacesContext context, String msg) {
        addMessage(context, FacesMessage.SEVERITY_INFO, msg, true);
        context.getExternalContext().getSessionMap().put(FacesUtils.HAS_INFO_MESSAGE, true);
    }

    public static void addInfoMessage(FacesContext context, String msg, boolean key) {
        if (key) {
            addInfoMessage(context, msg);
        } else {
            addMessage(context, FacesMessage.SEVERITY_INFO, msg, key);
            context.getExternalContext().getSessionMap().put(FacesUtils.HAS_INFO_MESSAGE, true);
        }
    }

    public static void addWarnMessage(FacesContext context, String msg) {
        addMessage(context, FacesMessage.SEVERITY_WARN, msg, true);
        context.getExternalContext().getSessionMap().put(FacesUtils.HAS_WARN_MESSAGE, true);
    }

    public static void addWarnMessage(FacesContext context, String msg, boolean key) {
        if (key) {
            addWarnMessage(context, msg);
        } else {
            addMessage(context, FacesMessage.SEVERITY_WARN, msg, key);
            context.getExternalContext().getSessionMap().put(FacesUtils.HAS_WARN_MESSAGE, true);
        }
    }

    public static void addErrorMessage(FacesContext context, String msg) {
        addMessage(context, FacesMessage.SEVERITY_ERROR, msg, true);
        context.getExternalContext().getSessionMap().put(FacesUtils.HAS_ERROR_MESSAGE, true);
    }

    public static void addErrorMessage(FacesContext context, String msg, boolean key) {
        if (key) {
            addErrorMessage(context, msg);
        } else {
            addMessage(context, FacesMessage.SEVERITY_ERROR, msg, key);
            context.getExternalContext().getSessionMap().put(FacesUtils.HAS_ERROR_MESSAGE, true);
        }
    }

    public static void addErrorMessage(FacesContext context, String msg, Exception e) {
        addMessage(context, FacesMessage.SEVERITY_ERROR, msg, e, true);
        context.getExternalContext().getSessionMap().put(FacesUtils.HAS_ERROR_MESSAGE, true);
    }

    private static void addMessage(FacesContext context, FacesMessage.Severity severity, String msg, boolean key) {
        addMessage(context, severity, msg, null, key);
    }

    public static void addMessage(FacesContext context, FacesMessage.Severity severity, String msg, Exception e,
            boolean key) {
        final String description;
        if (e != null) {
            description = e.toString();
        } else {
            description = msg;
        }
        if (key) {
            context.addMessage(null, new FacesMessage(severity, getLabel(context, msg, ""), description));
        } else {
            context.addMessage(null, new FacesMessage(severity, msg, description));
        }
    }

    public static String getLabel(FacesContext context, Locale locale, String key) {
        return getLabel(context, locale, key, "");
    }

    public static String getLabel(FacesContext context, String key) {
        return getLabel(context, key, "");
    }

    public static String getLabel(FacesContext context, String key, String defaultValue) {
        Locale locale = null;
        if (context != null && context.getViewRoot() != null) {
            locale = context.getViewRoot().getLocale();
        } else {
            locale = Locale.getDefault();
        }

        return getLabel(context, locale, key, defaultValue);
    }

    public static String getLabel(FacesContext context, Locale locale, String key, String defaultValue) {
        String res = null;
        if (context != null && !StringUtil.isBlank(key)) {
            try {
                ResourceBundle bundle = null;
                if (locale == null) {
                    bundle = getBundle(context, context.getViewRoot().getLocale(), getDefaultBundleName(context));
                } else {
                    bundle = getBundle(context, locale, getDefaultBundleName(context));
                }
                res = bundle.getString(key);
            } catch (final MissingResourceException ignore) {
                res = defaultValue;
            }
        }

        if (StringUtil.isBlank(res)) {
            res = defaultValue;
        }
        return res;
    }

    public static String getDefaultBundleName(FacesContext context) {
        if (StringUtil.isBlank(BUNDLE_NAME)) {
            Document document = null;
            try {
                document = getXml(context);
            } catch (final DocumentException e) {
                e.printStackTrace();
            }
            if (document != null) {
                try {
                    final Dom4jXPath xPath = evaluateXPath(document,
                            "//jee:application/jee:resource-bundle/jee:base-name");
                    if (xPath != null) {
                        final Node node = (Node) xPath.selectSingleNode(document);
                        if (node != null) {
                            BUNDLE_NAME = node.getText();
                        }
                    }
                } catch (final JaxenException e) {
                    e.printStackTrace();
                }
            }
        }
        return BUNDLE_NAME;
    }

    private static Document getXml(FacesContext context) throws DocumentException {
        Document document = null;
        String configFiles = context.getExternalContext().getInitParameter(CONFIG_FILES_KEY);
        if (StringUtil.isBlank(configFiles)) {
            configFiles = DEFAULT_CONFIG_FILES;
        }
        final String resource = context.getExternalContext().getRealPath(configFiles);
        if (resource != null) {
            final SAXReader reader = new SAXReader();
            document = reader.read(resource);
        }
        return document;
    }

    private static Dom4jXPath evaluateXPath(Document document, String xPathExpression) throws JaxenException {
        Dom4jXPath xPath = null;
        if (document != null && !StringUtil.isBlank(xPathExpression)) {
            xPath = new Dom4jXPath(xPathExpression);
            if (xPath != null) {
                final Map<String, String> map = new HashMap<String, String>();
                map.put("jee", "http://java.sun.com/xml/ns/javaee");

                xPath.setNamespaceContext(new SimpleNamespaceContext(map));
            }
        }
        return xPath;
    }

    public static String getMessageFormat(FacesContext context, String key, String defaultValue, Object... args) {
        String value = null;
        if (args != null) {
            final String pattern = getLabel(context, key, defaultValue);
            if (!StringUtil.isBlank(pattern)) {
                try {
                    value = MessageFormat.format(pattern, args);
                } catch (final IllegalArgumentException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(e.getMessage(), e);
                    }
                }
            }
        }
        return value;
    }

    private static ResourceBundle getBundle(FacesContext facesContext, Locale locale, String bundleName) {
        return ResourceBundle.getBundle(bundleName, locale, facesContext.getClass().getClassLoader());

    }

    public static UIComponent findComponentById(FacesContext facesContext, String componentId) {
        UIComponent component = null;
        if (facesContext != null && !StringUtil.isBlank(componentId)) {
            final List<UIComponent> components = facesContext.getViewRoot().getChildren();
            component = findComponentById(components, componentId);
        }
        return component;
    }

    private static UIComponent findComponentById(List<UIComponent> components, String componentId) {
        UIComponent component = null;
        if (!CollectionUtils.isEmpty(components) && !StringUtil.isBlank(componentId)) {
            final Iterator<UIComponent> componentsIterator = components.iterator();
            do {
                final UIComponent uiComponent = componentsIterator.next();
                if (uiComponent.getId().equals(componentId)) {
                    component = uiComponent;
                } else {
                    component = findComponentById(uiComponent.getChildren(), componentId);
                }
            } while (component == null && componentsIterator.hasNext());
        }
        return component;
    }

    public static String getCurrentLanguage(FacesContext facesContext) {
        String currentLanguage = null;
        if (facesContext != null) {
            final Locale locale = facesContext.getViewRoot().getLocale();
            currentLanguage = locale.getLanguage();
        }
        return currentLanguage;
    }

    public static Locale updateLocale(FacesContext facesContext, Locale locale) {
        Locale selectedLocale = null;
        if (facesContext != null) {
            if (locale != null) {
                final List<Locale> supportedLocales = IteratorUtils.toList(facesContext.getApplication()
                        .getSupportedLocales());
                if (!CollectionUtils.isEmpty(supportedLocales) && supportedLocales.contains(locale)) {
                    selectedLocale = locale;
                }
            }

            if (selectedLocale == null) {
                selectedLocale = facesContext.getApplication().getDefaultLocale();
            }

            facesContext.getViewRoot().setLocale(selectedLocale);
        }
        return selectedLocale;
    }

    public static Object getAttribute(FacesContext facesContext, String name) {
        Object value = null;
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final Map<Object, Object> attributeMap = facesContext.getAttributes();
            value = attributeMap.get(name);
        }
        return value;
    }

    public static void setAttribute(FacesContext facesContext, String name, Object value) {
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final Map<Object, Object> attributeMap = facesContext.getAttributes();
            attributeMap.put(name, value);
        }
    }

    public static void removeAttribute(FacesContext facesContext, String name) {
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final Map<Object, Object> attributeMap = facesContext.getAttributes();
            attributeMap.remove(name);
        }
    }

    public static HttpServletRequest getRequest(FacesContext facesContext) {
        HttpServletRequest request = null;
        if (facesContext != null) {
            request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        }
        return request;
    }

    public static String getRequestParameter(FacesContext facesContext, String name) {
        String value = null;
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
            value = requestParameterMap.get(name);
        }
        return value;
    }

    public static boolean containsRequestParameter(FacesContext facesContext, String name) {
        boolean contains = false;
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap();
            contains = requestParameterMap.containsKey(name);
        }
        return contains;
    }

    public static Object getRequestAttribute(FacesContext facesContext, String name) {
        Object value = null;
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            value = request.getAttribute(name);
        }
        return value;
    }

    public static void setRequestAttribute(FacesContext facesContext, String name, Object value) {
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            request.setAttribute(name, value);
        }
    }

    public static boolean containsRequestAttribute(FacesContext facesContext, String name) {
        boolean contains = false;
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            final Enumeration<String> attributeNames = request.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                final String attributeName = attributeNames.nextElement();
                if (attributeName.equals(name)) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    public static HttpSession getSession(FacesContext facesContext) {
        HttpSession session = null;
        if (facesContext != null) {
            session = (HttpSession) facesContext.getExternalContext().getSession(false);
        }
        return session;
    }

    public static Object getSessionAttribute(FacesContext facesContext, String name) {
        Object value = null;
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
            value = sessionMap.get(name);
        }
        return value;
    }

    public static void setSessionAttribute(FacesContext facesContext, String name, Object value) {
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
            sessionMap.put(name, value);
        }
    }

    public static void removeSessionAttribute(FacesContext facesContext, String name) {
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap();
            sessionMap.remove(name);
        }
    }

    public static boolean containsSessionAttribute(FacesContext facesContext, String name) {
        boolean contains = false;
        if (facesContext != null && !StringUtil.isBlank(name)) {
            final Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap();
            contains = sessionMap.containsKey(name);
        }
        return contains;
    }

    public static void invalidateSession(FacesContext facesContext) {
        final HttpSession session = getSession(facesContext);
        if (session != null) {
            session.invalidate();
        }
    }

    public static HttpServletResponse getResponse(FacesContext facesContext) {
        HttpServletResponse response = null;
        if (facesContext != null) {
            response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        }
        return response;
    }

    public static void callActionEventMethod(FacesContext facesContext, String method) {
        if (facesContext != null && !StringUtil.isBlank(method)) {
            final ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
            final ELContext elContext = facesContext.getELContext();
            final MethodExpression methodExpression = expressionFactory.createMethodExpression(elContext, method,
                    void.class, new Class[] { ActionEvent.class });
            if (methodExpression != null) {
                final UICommand component = new UICommand();
                final ActionEvent actionEvent = new ActionEvent(component);
                methodExpression.invoke(elContext, new Object[] { actionEvent });
            }
        }
    }

    public static <T> T getBean(FacesContext facesContext, String beanName, Class<T> beanClass) {
        T bean = null;
        final ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        final ValueExpression valueExpression = expressionFactory.createValueExpression(facesContext.getELContext(),
                "#{" + beanName + "}", beanClass);
        if (valueExpression != null) {
            bean = (T) valueExpression.getValue(facesContext.getELContext());
        }
        return bean;
    }

    public static void redirect(FacesContext facesContext, String path, boolean includeViewParams) throws IOException {
        if (facesContext != null && !StringUtil.isBlank(path)) {
            final String url = getUrl(facesContext, path, includeViewParams);
            if (!StringUtil.isBlank(url)) {
                facesContext.getExternalContext().redirect(url);
            }
        }
    }

    public static void handleNavigation(FacesContext facesContext, String outcome) {
        if (facesContext != null && !StringUtil.isBlank(outcome)) {
            final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

            navigationHandler.handleNavigation(facesContext, null, outcome);
            facesContext.renderResponse();
        }
    }

    public static String getViewId(FacesContext facesContext) {
        String viewId = null;
        if (facesContext != null) {
            viewId = facesContext.getViewRoot().getViewId();
        }
        return viewId;
    }

    public static String getActionExpression(UICommand component) {
        String action = null;
        if (component != null && component.getActionExpression() != null) {
            final String actionExpression = component.getActionExpression().getExpressionString();
            if (!StringUtil.isBlank(actionExpression)) {
                action = StringUtil.remove(actionExpression, "?faces-redirect=true&includeViewParams=true");
            }
        }
        return action;
    }

    public static String getAction(String path, boolean includeViewParams) {
        String action = null;
        if (!StringUtil.isBlank(path)) {
            if (StringUtil.endsWith(path, ".xhtml")) {
                action = StringUtil.removeEndIgnoreCase(path, ".xhtml");
            } else {
                action = path;
            }
            if (includeViewParams) {
                action += "?faces-redirect=true&includeViewParams=true";
            }
        }
        return action;
    }

    public static String getUrl(FacesContext facesContext, String path, boolean includeViewParams) {
        String url = null;
        if (facesContext != null && !StringUtil.isBlank(path)) {
            url = facesContext.getExternalContext().getRequestContextPath() + "/faces";
            if (!StringUtil.startsWith(path, "/")) {
                url += "/";
            }
            url += path;
            if (!StringUtil.endsWith(path, ".xhtml")) {
                url += ".xhtml";
            }
            if (includeViewParams) {
                url += "?faces-redirect=true&includeViewParams=true";
            }
        }
        return url;
    }

    public static String getActionURL(FacesContext facesContext) {
        String actionURL = null;
        if (facesContext != null) {
            final String viewId = getViewId(facesContext);
            if (!StringUtil.isBlank(viewId)) {
                actionURL = facesContext.getApplication().getViewHandler().getActionURL(facesContext, viewId);
                actionURL = facesContext.getExternalContext().encodeActionURL(actionURL);
            }
        }
        return actionURL;
    }
}
