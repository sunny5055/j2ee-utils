package com.google.code.jee.utils.xml;

import java.util.List;

public class GenericList<T> {
    private List<T> elements;

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    } 
}
