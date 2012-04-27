package com.google.code.jee.utils.jsf.bean.form;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.google.code.jee.utils.dal.Result;
import com.google.code.jee.utils.dal.dto.Dto;
import com.google.code.jee.utils.dal.service.GenericService;
import com.google.code.jee.utils.jsf.util.FacesUtils;
import com.google.code.jee.utils.jsf.util.RequestContextUtil;

/**
 * The Class AbstractCreateFormBean.
 * 
 * @param <PK> the generic type
 * @param <E> the element type
 * @param <S> the generic type
 */
@SuppressWarnings("serial")
public abstract class AbstractCreateFormBean<PK extends Serializable, E extends Dto<PK>, S extends GenericService<PK, E>>
        implements Serializable {
    protected static final Logger LOGGER = Logger.getLogger(AbstractCreateFormBean.class);
    public static final String IS_VALID = "isValid";
    public static final String EDIT_MODE = "editMode";
    protected E entity;

    /**
     * Instantiates a new abstract create form bean.
     */
    public AbstractCreateFormBean() {
    }

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        entity = getNewEntityInstance();
    }

    /**
     * Gets the service.
     * 
     * @return the service
     */
    public abstract S getService();

    /**
     * Gets the entity.
     * 
     * @return the entity
     */
    public E getEntity() {
        return entity;
    }

    /**
     * Sets the entity.
     * 
     * @param entity the new entity
     */
    public void setEntity(E entity) {
        this.entity = entity;
    }

    /**
     * Gets the new entity instance.
     * 
     * @return the new entity instance
     */
    protected abstract E getNewEntityInstance();

    /**
     * Gets the list page.
     * 
     * @return the list page
     */
    protected abstract String getListPage();

    /**
     * Gets the view page.
     * 
     * @return the view page
     */
    protected String getViewPage() {
        return "";
    }

    /**
     * Gets the id parameter name.
     * 
     * @return the id parameter name
     */
    protected abstract String getIdParameterName();

    /**
     * Re init.
     */
    protected void reInit() {

    }

    /**
     * Re init.
     * 
     * @param event the event
     */
    public void reInit(ActionEvent event) {
        this.reInit();
    }

    /**
     * Prepare create.
     * 
     * @return true, if successful
     */
    protected abstract boolean prepareCreate();

    /**
     * Creates the.
     * 
     * @param event the event
     */
    public void create(ActionEvent event) {
        if (entity != null) {
            if (prepareCreate()) {
                final Result<E> result = this.getService().create(entity);
                handleCreate(result);
            }
        }
    }

    /**
     * Handle create.
     * 
     * @param result the result
     */
    protected void handleCreate(final Result<E> result) {
        if (result != null && result.isValid()) {
            entity = result.getValue();
            reInit();

            RequestContextUtil.addCallbackParam(IS_VALID, true);
        }
    }

    /**
     * Redirect to list.
     */
    protected void redirectToList() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            FacesUtils.redirect(facesContext, getListPage(), true);
        } catch (final IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            }
        }
    }

    /**
     * Redirect to view.
     */
    protected void redirectToView() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            FacesUtils.setCustomScopeParameter(facesContext, EDIT_MODE, false);
            FacesUtils.setCustomScopeParameter(facesContext, getIdParameterName(), entity.getPrimaryKey());

            FacesUtils.redirect(facesContext, getViewPage(), true);
        } catch (final IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            }
        }
    }
}