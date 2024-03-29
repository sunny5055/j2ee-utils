package com.googlecode.jutils.jsf.bean.form;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

import com.googlecode.jutils.BooleanUtil;
import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericService;
import com.googlecode.jutils.dal.util.HibernateUtil;
import com.googlecode.jutils.jsf.bean.ErrorBean;
import com.googlecode.jutils.jsf.util.FacesUtils;
import com.googlecode.jutils.jsf.util.RequestContextUtil;

/**
 * The Class AbstractFormBean.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 * @param <S>
 *            the generic type
 */
@SuppressWarnings("serial")
public abstract class AbstractFormBean<PK extends Serializable, E extends Dto<PK>, S extends GenericService<PK, E>> extends AbstractCreateFormBean<PK, E, S> {
	protected boolean editMode;

	/**
	 * Instantiates a new abstract form bean.
	 */
	public AbstractFormBean() {
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	@PostConstruct
	protected void init() {
		entity = getEntityFromRequest();

		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final Object parameter = FacesUtils.getCustomScopeParameter(facesContext, AbstractCreateFormBean.EDIT_MODE);
		if (parameter != null && BooleanUtil.toBooleanObject(parameter)) {
			editMode = true;
		}

		if (entity == null) {
			entity = getNewEntityInstance();
		}
	}

	/**
	 * Gets the primary key.
	 * 
	 * @return the primary key
	 */
	public abstract PK getPrimaryKey();

	/**
	 * Gets the form properties.
	 * 
	 * @return the form properties
	 */
	public abstract String[] getFormProperties();

	/**
	 * Checks if is edits the mode.
	 * 
	 * @return true, if is edits the mode
	 */
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * Sets the edits the mode.
	 * 
	 * @param editMode
	 *            the new edits the mode
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	/**
	 * Gets the entity from request.
	 * 
	 * @return the entity from request
	 */
	public E getEntityFromRequest() {
		E dto = null;
		final PK pk = getPrimaryKey();
		if (pk != null) {
			dto = this.getService().get(pk);
		}
		return dto;
	}

	/**
	 * Edits the mode.
	 * 
	 * @param event
	 *            the event
	 */
	public void editMode(ActionEvent event) {
		setEditMode(true);

		RequestContextUtil.addCallbackParam(AbstractCreateFormBean.IS_VALID, true);
	}

	/**
	 * Prepare update.
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean prepareUpdate();

	/**
	 * Updates the.
	 * 
	 * @param event
	 *            the event
	 */
	public void update(ActionEvent event) {
		if (entity != null && entity.getPrimaryKey() != null) {
			if (prepareUpdate()) {
				E hibernateEntity = this.getService().get(entity.getPrimaryKey());
				if (hibernateEntity != null) {
					E result = null;
					try {
						hibernateEntity = HibernateUtil.copyEntity(entity, hibernateEntity, getFormProperties());
						result = this.getService().update(hibernateEntity);
					} catch (final IllegalAccessException e) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(e.getMessage(), e);
						}
					} catch (final InvocationTargetException e) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(e.getMessage(), e);
						}
					} catch (final NoSuchMethodException e) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(e.getMessage(), e);
						}
					}

					handleUpdate(result);
				} else {
					final FacesContext facesContext = FacesContext.getCurrentInstance();
					FacesUtils.addErrorMessage(facesContext, "error_unable_to_find_entity");
				}
			}
		}
	}

	/**
	 * Handle update.
	 * 
	 * @param result
	 *            the result
	 */
	protected void handleUpdate(final E result) {
		if (result != null) {
			entity = result;
			reInit();

			RequestContextUtil.addCallbackParam(AbstractCreateFormBean.IS_VALID, true);
		}
		setEditMode(true);
	}

	/**
	 * Can delete.
	 * 
	 * @return true, if successful
	 */
	public abstract boolean canDelete();

	/**
	 * Gets the primary key to delete.
	 * 
	 * @return the primary key to delete
	 */
	public abstract PK getPrimaryKeyToDelete();

	/**
	 * Deletes the.
	 */
	public void delete() {
		if (!canDelete()) {
			redirectNoAccess();
		} else {
			final PK primaryKey = getPrimaryKeyToDelete();
			if (primaryKey != null) {
				Integer result = null;
				if (this.getService().isRemovable(primaryKey)) {
					result = this.getService().deleteByPrimaryKey(primaryKey);
					handleDelete(result);
				}
			}
		}
	}

	/**
	 * Handle delete.
	 * 
	 * @param result
	 *            the result
	 */
	protected void handleDelete(final Integer result) {
		if (result != null) {
			final RequestContext requestContext = RequestContext.getCurrentInstance();
			if (requestContext != null) {
				requestContext.addCallbackParam(AbstractCreateFormBean.IS_VALID, true);
			}
		}
	}

	/**
	 * Redirect no access.
	 */
	protected void redirectNoAccess() {
		redirectToErrorPage("error_no_access_title", "error_no_access_content");
	}

	/**
	 * Gets the error page.
	 * 
	 * @return the error page
	 */
	protected abstract String getErrorPage();

	/**
	 * Redirect to error page.
	 * 
	 * @param titleKey
	 *            the title key
	 * @param messageKey
	 *            the message key
	 */
	protected void redirectToErrorPage(String titleKey, String messageKey) {
		if (!StringUtil.isBlank(titleKey) && !StringUtil.isBlank(messageKey)) {
			final FacesContext facesContext = FacesContext.getCurrentInstance();
			final ErrorBean errorBean = FacesUtils.getBean(facesContext, "errorBean", ErrorBean.class);
			if (errorBean != null) {
				final String title = FacesUtils.getLabel(facesContext, titleKey);
				errorBean.setTitle(title);

				final String message = FacesUtils.getLabel(facesContext, messageKey);
				errorBean.setMessage(message);
			}

			try {
				FacesUtils.redirect(facesContext, getErrorPage(), true);
			} catch (final IOException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(e.getMessage(), e);
				}
			}
		}
	}
}