package com.google.code.jee.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * The Class AbstractUnicodeResourceBundle.
 */
public abstract class AbstractUnicodeResourceBundle extends ResourceBundle {
    protected static final String BUNDLE_EXTENSION = "properties";
    protected static final Control UTF8_CONTROL = new UTF8Control();

    /**
     * Instantiates a new abstract unicode resource bundle.
     */
    public AbstractUnicodeResourceBundle() {
        final ResourceBundle bundle = ResourceBundle.getBundle(getBundleName(), Locale.getDefault(), UTF8_CONTROL);
        setParent(bundle);
    }

    /**
     * Gets the bundle name.
     * 
     * @return the bundle name
     */
    protected abstract String getBundleName();

    /**
     * {@inheritedDoc}
     */
    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Enumeration<String> getKeys() {
        return parent.getKeys();
    }

    /**
     * The Class UTF8Control.
     */
    protected static class UTF8Control extends Control {
        private static final String UTF_8 = "UTF-8";

        /**
         * {@inheritedDoc}
         */
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
                boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            final String bundleName = toBundleName(baseName, locale);
            final String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                final URL url = loader.getResource(resourceName);
                if (url != null) {
                    final URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}