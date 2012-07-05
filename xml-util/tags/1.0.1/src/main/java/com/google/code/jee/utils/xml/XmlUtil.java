package com.google.code.jee.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
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
        XStream xStream = null;

        if (!StringUtil.isEmpty(rootNodeName) && !StringUtil.isEmpty(listFieldName)) {
            xStream = new XStream(new DomDriver(UTF8));
            xStream.alias(rootNodeName, GenericList.class);
            xStream.addImplicitCollection(GenericList.class, listFieldName);
        }

        return xStream;
    }

    /**
     * Adds the alias for class.
     * 
     * @param xStream the x stream
     * @param clazz the clazz
     */
    public static void addAliasForClass(XStream xStream, Class<?> clazz) {
        if (xStream != null && clazz != null) {
            xStream.alias(StringUtil.toLowercaseFirstCharacter(clazz.getSimpleName()), clazz);
        }
    }

    /**
     * Sets the attributes for.
     * 
     * @param xStream the x stream
     * @param clazz the clazz
     * @param fieldNames the field names
     */
    public static void setAttributesFor(XStream xStream, Class<?> clazz, String... fieldNames) {
        if (!ArrayUtil.isEmpty(fieldNames)) {
            setAttributesFor(xStream, clazz, Arrays.asList(fieldNames));
        }
    }

    /**
     * Sets the attributes.
     * 
     * @param xStream the x stream
     * @param clazz the clazz
     * @param fieldNames the field names
     */
    public static void setAttributesFor(XStream xStream, Class<?> clazz, List<String> fieldNames) {
        if (xStream != null && !CollectionUtil.isEmpty(fieldNames)) {
            for (final String fieldName : fieldNames) {
                xStream.useAttributeFor(clazz, fieldName);
            }
        }
    }

    /**
     * Omit fields.
     * 
     * @param xStream the x stream
     * @param clazz the clazz
     * @param fieldNames the field names
     */
    public static void omitFields(XStream xStream, Class<?> clazz, String... fieldNames) {
        if (!ArrayUtil.isEmpty(fieldNames)) {
            omitFields(xStream, clazz, Arrays.asList(fieldNames));
        }
    }

    /**
     * Omit fields.
     * 
     * @param xStream the x stream
     * @param clazz the clazz
     * @param fieldNames the field names
     */
    public static void omitFields(XStream xStream, Class<?> clazz, List<String> fieldNames) {
        if (xStream != null && !CollectionUtil.isEmpty(fieldNames)) {
            for (final String fieldName : fieldNames) {
                xStream.omitField(clazz, fieldName);
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
            final GenericList genericList = new GenericList();
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
            final GenericList genericList = (GenericList) xStream.fromXML(inputStream);

            if (genericList != null) {
                elements = genericList.getElements();
            }
        }
        return elements;
    }
}
