package com.google.code.jee.utils.jsf.bean.datatable;

import java.io.Serializable;

import com.google.code.jee.utils.dal.dto.Dto;

/**
 * The Class SingleRowSelectionDataTableBean.
 * 
 * @param <PK> the entity primary key type
 * @param <E> the entity type
 * @param <S> the service type
 */
@SuppressWarnings("serial")
public abstract class AbstractSingleRowSelectionDataTableBean<PK extends Serializable, E extends Dto<PK>> extends
        AbstractDataTableBean {
    protected E selectedObject;

    /**
     * Gets the selected object.
     * 
     * @return the selected object
     */
    public E getSelectedObject() {
        return selectedObject;
    }

    /**
     * Sets the select object.
     * 
     * @param newSelectedObject the new selected object
     */
    public void setSelectedObject(E newSelectedObject) {
        this.selectedObject = newSelectedObject;
    }
}
