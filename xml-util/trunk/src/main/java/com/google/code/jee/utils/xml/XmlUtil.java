package com.google.code.jee.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.collection.MapUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtil {
    private static final String UTF8 = "UTF-8";
    private static final String ROOT_NODE_NAME = "data";
    private static final String LIST_FIELD_NAME = "elements";

    /**
     * Inits the stream.
     * 
     * @return the x stream
     */
    public static XStream getStream() {
        return getStream(ROOT_NODE_NAME, LIST_FIELD_NAME);
    }

    /**
     * Inits the stream.
     * 
     * @param rootNodeName the root node name
     * @param listFieldName the list field name
     * @return the x stream
     */
    public static XStream getStream(String rootNodeName, String listFieldName) {
        XStream xStream = new XStream(new DomDriver(UTF8));
        xStream.alias(rootNodeName, GenericList.class);
        xStream.addImplicitCollection(GenericList.class, listFieldName);

        return xStream;
    }

    /**
     * Sets the aliases.
     * 
     * @param xStream the x stream
     * @param classNames the class names
     */
    public static void setAliases(XStream xStream, Map<String, Class<?>> classNames) {
        if (xStream != null && !MapUtil.isEmpty(classNames)) {
            for (Map.Entry<String, Class<?>> entry : classNames.entrySet()) {
                xStream.alias(entry.getKey(), entry.getValue());
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
        if (xStream != null && !MapUtil.isEmpty(classNames)) {
            for (Map.Entry<Class<?>, List<String>> entry : classNames.entrySet()) {
                for (String attribute : entry.getValue()) {
                    xStream.useAttributeFor(entry.getKey(), attribute);
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
        if (xStream != null && !MapUtil.isEmpty(classNames)) {
            for (Map.Entry<Class<?>, List<String>> entry : classNames.entrySet()) {
                for (String attribute : entry.getValue()) {
                    xStream.omitField(entry.getKey(), attribute);
                }
            }
        }
    }

    /**
     * Export to xml.
     * 
     * @param xStream the x stream
     * @param outputStream the output stream
     * @param elements the types
     * 
     * @param <T> the generic type
     */
    public static void exportToXml(XStream xStream, OutputStream outputStream, List<?> elements) {
        if (xStream != null && outputStream != null && !CollectionUtil.isEmpty(elements)) {
            GenericList genericList = new GenericList();
            genericList.setElements(elements);
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
        List<?> elements = null;
        if (xStream != null && inputStream != null) {
            GenericList genericList = (GenericList) xStream.fromXML(inputStream);

            if (genericList != null) {
                elements = genericList.getElements();
            }
        }
        return elements;
    }
}
