package com.google.code.jee.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.collection.MapUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtil {

    /**
     * Inits the stream.
     * 
     * @param xStream the x stream
     * @return the x stream
     */
    public static XStream initStream(XStream xStream) {
        return xStream = new XStream(new DomDriver("UTF-8"));
    }

    /**
     * Sets the aliases.
     * 
     * @param <T> the generic type
     * @param xStream the x stream
     * @param classNames the class names
     */
    public static <T> void setAliases(XStream xStream, Map<String, Class<T>> classNames) {
        if (!MapUtil.isEmpty(classNames)) {
            Iterator<String> iter = classNames.keySet().iterator();

            while (iter.hasNext()) {
                String name = (String) iter.next();
                xStream.alias(name, classNames.get(name));
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
    public static <T> void exportToXml(XStream xStream, String rootNodeName, List<T> types, OutputStream outputStream) {
        if (!CollectionUtil.isEmpty(types)) {
            // xStream.alias(rootNodeName, List.class);
            // xStream.addImplicitCollection(List.class, rootNodeName);
            xStream.alias(rootNodeName, GenericList.class);
            xStream.addImplicitCollection(GenericList.class, rootNodeName);

            GenericList<T> genericList = new GenericList<T>();
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
    public static <T> void importToXml(XStream xStream, List<T> types, InputStream inputStream) throws IOException {
        if (inputStream != null) {
            GenericList<Employee> genericList = (GenericList<Employee>) xStream.fromXML(inputStream);
            types.addAll((Collection<? extends T>) genericList.getElements());
        }
    }
}
