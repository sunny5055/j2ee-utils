package com.google.code.jee.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.collection.MapUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtil {

    private static String UTF8 = "UTF-8";

    /**
     * Inits the stream.
     * 
     * @param xStream the x stream
     * @return the x stream
     */
    public static XStream initStream(XStream xStream) {
        return xStream = new XStream(new DomDriver(UTF8));
    }

    /**
     * Sets the aliases.
     * 
     * @param xStream the x stream
     * @param classNames the class names
     */
    public static void setAliases(XStream xStream, Map<String, Class<?>> classNames) {
        if (!MapUtil.isEmpty(classNames)) {
            Iterator<String> iter = classNames.keySet().iterator();

            while (iter.hasNext()) {
                String name = (String) iter.next();
                xStream.alias(name, classNames.get(name));
            }
        }
    }

    /**
     * Sets the attributes.
     * 
     * @param xStream the x stream
     * @param classNames the class names
     */
    public static void setAttributesFor(XStream xStream, Map<Class<?>, List<String>> classNames) {
        if (!MapUtil.isEmpty(classNames)) {
            Iterator<Class<?>> iter = classNames.keySet().iterator();

            while (iter.hasNext()) {
                Class<?> className = (Class<?>) iter.next();
                for (String attribute : classNames.get(className)) {
                    xStream.useAttributeFor(attribute, className);
                }
            }
        }
    }

    /**
     * Omit fields.
     * 
     * @param xStream the x stream
     * @param classNames the class names
     */
    public static void omitFields(XStream xStream, Map<Class<?>, List<String>> classNames) {
        if (!MapUtil.isEmpty(classNames)) {
            Iterator<Class<?>> iter = classNames.keySet().iterator();

            while (iter.hasNext()) {
                Class<?> className = (Class<?>) iter.next();
                for (String field : classNames.get(className)) {
                    xStream.omitField(className, field);
                }
            }
        }
    }

    /**
     * Export to xml.
     * 
     * @param <T> the generic type
     * @param xStream the x stream
     * @param types the types
     * @param outputStream the output stream
     */
    public static <T> void exportToXml(XStream xStream, String rootNodeName, String listFieldName, List<T> types,
            OutputStream outputStream) {
        if (!CollectionUtil.isEmpty(types)) {
            xStream.alias(rootNodeName, GenericList.class);
            xStream.addImplicitCollection(GenericList.class, listFieldName);

            GenericList genericList = new GenericList();
            genericList.setElements(types);
            xStream.toXML(genericList, outputStream);
        }
    }

    /**
     * Import to xml.
     * 
     * @param <T> the generic type
     * @param xStream the x stream
     * @param types the types
     * @param inputStream the input stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static List<?> importToXml(XStream xStream, InputStream inputStream) throws IOException {
        GenericList genericList = null;
        if (inputStream != null) {
            genericList = (GenericList) xStream.fromXML(inputStream);
        }
        return genericList.getElements();
    }
}
