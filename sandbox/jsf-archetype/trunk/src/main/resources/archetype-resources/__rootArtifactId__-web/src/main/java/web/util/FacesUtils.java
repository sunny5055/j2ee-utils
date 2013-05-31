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
import javax.faces.context.Flash;
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

	public static FacesMessage getMessage(FacesMessage.Severity severity, String msg) {
		FacesMessage facesMessage = null;
		if (severity != null && !StringUtil.isBlank(msg)) {
			final String label = getLabel(msg);
			facesMessage = new FacesMessage(severity, label, label);
		}
		return facesMessage;
	}

	public static void addMessageIntoSession(FacesMessage.Severity severity, String msg) {
		final FacesMessage facesMessage = getMessage(severity, msg);
		if (facesMessage != null) {
			final List<FacesMessage> existingMessages = (List<FacesMessage>) FacesUtils.getSessionAttribute(FacesUtils.MSGS_KEY);
			if (!CollectionUtil.isEmpty(existingMessages)) {
				existingMessages.add(facesMessage);
			} else {
				FacesUtils.setSessionAttribute(FacesUtils.MSGS_KEY, Arrays.asList(facesMessage));
			}
		}
	}

	public static void addInfoMessage(String msg) {
		addMessage(FacesMessage.SEVERITY_INFO, msg, true);
		FacesUtils.setSessionAttribute(FacesUtils.HAS_INFO_MESSAGE, true);
	}

	public static void addInfoMessage(String msg, boolean key) {
		if (key) {
			addInfoMessage(msg);
		} else {
			addMessage(FacesMessage.SEVERITY_INFO, msg, key);
			FacesUtils.setSessionAttribute(FacesUtils.HAS_INFO_MESSAGE, true);
		}
	}

	public static void addWarnMessage(String msg) {
		addMessage(FacesMessage.SEVERITY_WARN, msg, true);
		FacesUtils.setSessionAttribute(FacesUtils.HAS_WARN_MESSAGE, true);
	}

	public static void addWarnMessage(String msg, boolean key) {
		if (key) {
			addWarnMessage(msg);
		} else {
			addMessage(FacesMessage.SEVERITY_WARN, msg, key);
			FacesUtils.setSessionAttribute(FacesUtils.HAS_WARN_MESSAGE, true);
		}
	}

	public static void addErrorMessage(String msg) {
		addMessage(FacesMessage.SEVERITY_ERROR, msg, true);
		FacesUtils.setSessionAttribute(FacesUtils.HAS_ERROR_MESSAGE, true);
	}

	public static void addErrorMessage(String msg, boolean key) {
		if (key) {
			addErrorMessage(msg);
		} else {
			addMessage(FacesMessage.SEVERITY_ERROR, msg, key);
			FacesUtils.setSessionAttribute(FacesUtils.HAS_ERROR_MESSAGE, true);
		}
	}

	public static void addErrorMessage(String msg, Exception e) {
		addMessage(FacesMessage.SEVERITY_ERROR, msg, e, true);
		FacesUtils.setSessionAttribute(FacesUtils.HAS_ERROR_MESSAGE, true);
	}

	private static void addMessage(FacesMessage.Severity severity, String msg, boolean key) {
		addMessage(severity, msg, null, key);
	}

	public static void addMessage(FacesMessage.Severity severity, String msg, Exception e, boolean key) {
		String description = null;
		if (e != null) {
			description = e.toString();
		} else {
			description = msg;
		}
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (key) {
			facesContext.addMessage(null, new FacesMessage(severity, getLabel(msg, ""), description));
		} else {
			facesContext.addMessage(null, new FacesMessage(severity, msg, description));
		}
	}

	public static String getLabel(Locale locale, String key) {
		return getLabel(locale, key, "");
	}

	public static String getLabel(String key) {
		return getLabel(key, "");
	}

	public static String getLabel(String key, String defaultValue) {
		Locale locale = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && facesContext.getViewRoot() != null) {
			locale = facesContext.getViewRoot().getLocale();
		} else {
			locale = Locale.getDefault();
		}

		return getLabel(locale, key, defaultValue);
	}

	public static String getLabel(Locale locale, String key, String defaultValue) {
		String res = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(key)) {
			try {
				ResourceBundle bundle = null;
				if (locale == null) {
					bundle = getBundle(facesContext.getViewRoot().getLocale(), getDefaultBundleName());
				} else {
					bundle = getBundle(locale, getDefaultBundleName());
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

	public static String getDefaultBundleName() {
		if (StringUtil.isBlank(BUNDLE_NAME)) {
			Document document = null;
			try {
				document = getXml();
			} catch (final DocumentException e) {
				e.printStackTrace();
			}
			if (document != null) {
				try {
					final Dom4jXPath xPath = evaluateXPath(document, "//jee:application/jee:resource-bundle/jee:base-name");
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

	private static Document getXml() throws DocumentException {
		Document document = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			String configFiles = facesContext.getExternalContext().getInitParameter(CONFIG_FILES_KEY);
			if (StringUtil.isBlank(configFiles)) {
				configFiles = DEFAULT_CONFIG_FILES;
			}
			final String resource = facesContext.getExternalContext().getRealPath(configFiles);
			if (resource != null) {
				final SAXReader reader = new SAXReader();
				document = reader.read(resource);
			}
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

	public static String getMessageFormat(String key, String defaultValue, Object... args) {
		String value = null;
		if (args != null) {
			final String pattern = getLabel(key, defaultValue);
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

	private static ResourceBundle getBundle(Locale locale, String bundleName) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		return ResourceBundle.getBundle(bundleName, locale, facesContext.getClass().getClassLoader());

	}

	public static UIComponent findComponentById(String componentId) {
		UIComponent component = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
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

	public static String getCurrentLanguage() {
		String currentLanguage = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			final Locale locale = facesContext.getViewRoot().getLocale();
			currentLanguage = locale.getLanguage();
		}
		return currentLanguage;
	}

	public static Locale updateLocale(Locale locale) {
		Locale selectedLocale = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			if (locale != null) {
				final List<Locale> supportedLocales = IteratorUtils.toList(facesContext.getApplication().getSupportedLocales());
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

	public static Object getAttribute(String name) {
		Object value = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(name)) {
			final Map<Object, Object> attributeMap = facesContext.getAttributes();
			value = attributeMap.get(name);
		}
		return value;
	}

	public static void setAttribute(String name, Object value) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(name)) {
			final Map<Object, Object> attributeMap = facesContext.getAttributes();
			attributeMap.put(name, value);
		}
	}

	public static void removeAttribute(String name) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(name)) {
			final Map<Object, Object> attributeMap = facesContext.getAttributes();
			attributeMap.remove(name);
		}
	}

	public static HttpServletRequest getRequest() {
		HttpServletRequest request = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		}
		return request;
	}

	public static String getRequestParameter(String name) {
		String value = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(name)) {
			final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
			value = requestParameterMap.get(name);
		}
		return value;
	}

	public static boolean containsRequestParameter(String name) {
		boolean contains = false;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(name)) {
			final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
			contains = requestParameterMap.containsKey(name);
		}
		return contains;
	}

	public static Object getRequestAttribute(String name) {
		Object value = null;
		final HttpServletRequest request = getRequest();
		if (request != null && !StringUtil.isBlank(name)) {
			value = request.getAttribute(name);
		}
		return value;
	}

	public static void setRequestAttribute(String name, Object value) {
		final HttpServletRequest request = getRequest();
		if (request != null && !StringUtil.isBlank(name)) {
			request.setAttribute(name, value);
		}
	}

	public static boolean containsRequestAttribute(String name) {
		boolean contains = false;
		final HttpServletRequest request = getRequest();
		if (request != null && !StringUtil.isBlank(name)) {
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

	public static HttpSession getSession() {
		HttpSession session = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			session = (HttpSession) facesContext.getExternalContext().getSession(false);
		}
		return session;
	}

	public static Object getSessionAttribute(String name) {
		Object value = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(name)) {
			final Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
			value = sessionMap.get(name);
		}
		return value;
	}

	public static void setSessionAttribute(String name, Object value) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(name)) {
			final Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
			sessionMap.put(name, value);
		}
	}

	public static void removeSessionAttribute(String name) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(name)) {
			final Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
			sessionMap.remove(name);
		}
	}

	public static boolean containsSessionAttribute(String name) {
		boolean contains = false;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(name)) {
			final Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
			contains = sessionMap.containsKey(name);
		}
		return contains;
	}

	public static void invalidateSession() {
		final HttpSession session = getSession();
		if (session != null) {
			session.invalidate();
		}
	}

	public static Flash getFlash() {
		Flash flash = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			flash = facesContext.getExternalContext().getFlash();
		}
		return flash;
	}

	public static Object getFlashAttribute(String name) {
		Object value = null;
		final Flash flash = getFlash();
		if (flash != null && !StringUtil.isBlank(name)) {
			value = flash.get(name);
		}
		return value;
	}

	public static void setFlashAttribute(String name, Object value) {
		final Flash flash = getFlash();
		if (flash != null && !StringUtil.isBlank(name)) {
			flash.put(name, value);
		}
	}

	public static void removeFlashAttribute(String name) {
		final Flash flash = getFlash();
		if (flash != null && !StringUtil.isBlank(name)) {
			flash.remove(name);
		}
	}

	public static boolean containsFlashAttribute(String name) {
		boolean contains = false;
		final Flash flash = getFlash();
		if (flash != null && !StringUtil.isBlank(name)) {
			contains = flash.containsKey(name);
		}
		return contains;
	}

	public static HttpServletResponse getResponse() {
		HttpServletResponse response = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		}
		return response;
	}

	public static void callActionEventMethod(String method) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(method)) {
			final ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
			final ELContext elContext = facesContext.getELContext();
			final MethodExpression methodExpression = expressionFactory.createMethodExpression(elContext, method, void.class, new Class[] { ActionEvent.class });
			if (methodExpression != null) {
				final UICommand component = new UICommand();
				final ActionEvent actionEvent = new ActionEvent(component);
				methodExpression.invoke(elContext, new Object[] { actionEvent });
			}
		}
	}

	public static <T> T getBean(String beanName, Class<T> beanClass) {
		T bean = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(beanName) && beanClass != null) {
			final ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
			final ValueExpression valueExpression = expressionFactory.createValueExpression(facesContext.getELContext(), "#{" + beanName + "}", beanClass);
			if (valueExpression != null) {
				bean = (T) valueExpression.getValue(facesContext.getELContext());
			}
		}
		return bean;
	}

	public static void redirect(String path, boolean includeViewParams) throws IOException {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(path)) {
			final String url = getUrl(path, includeViewParams);
			if (!StringUtil.isBlank(url)) {
				facesContext.getExternalContext().redirect(url);
			}
		}
	}

	public static void handleNavigation(String outcome) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null && !StringUtil.isBlank(outcome)) {
			final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

			navigationHandler.handleNavigation(facesContext, null, outcome);
			facesContext.renderResponse();
		}
	}

	public static String getViewId() {
		String viewId = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
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

	public static String getUrl(String path, boolean includeViewParams) {
		String url = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
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

	public static String getActionURL() {
		String actionURL = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			final String viewId = getViewId();
			if (!StringUtil.isBlank(viewId)) {
				actionURL = facesContext.getApplication().getViewHandler().getActionURL(facesContext, viewId);
				actionURL = facesContext.getExternalContext().encodeActionURL(actionURL);
			}
		}
		return actionURL;
	}
}
