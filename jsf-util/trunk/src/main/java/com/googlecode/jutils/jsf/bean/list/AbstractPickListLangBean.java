package com.googlecode.jutils.jsf.bean.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.model.DualListModel;

import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericReadService;

/**
 * An abstract class for using the picklist component.
 * 
 * @param <RPK>
 *            the parent entity primary key type
 * @param <PK>
 *            the entity primary key type
 * @param <E>
 *            the entity type
 * @param <S>
 *            the service type
 */
@SuppressWarnings("serial")
public abstract class AbstractPickListLangBean<RPK extends Serializable, PK extends Serializable, E extends Dto<PK>, S extends GenericReadService<PK, E>> implements Serializable {
	/** The parent primary key. */
	protected RPK parentPrimaryKey;

	/** The target entities. */
	protected List<E> targetEntities;

	/** The dual list model. */
	protected DualListModel<E> entities;

	protected boolean loaded;

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		final List<E> sourceEntities = new ArrayList<E>();
		targetEntities = new ArrayList<E>();
		entities = new DualListModel<E>(sourceEntities, targetEntities);
	}

	/**
	 * Getter : return the parentPrimaryKey.
	 * 
	 * @return the parentPrimaryKey
	 */
	public RPK getParentPrimaryKey() {
		return parentPrimaryKey;
	}

	/**
	 * Setter : affect the parentPrimaryKey.
	 * 
	 * @param parentPrimaryKey
	 *            the parentPrimaryKey
	 */
	public void setParentPrimaryKey(RPK parentPrimaryKey) {
		this.parentPrimaryKey = parentPrimaryKey;
	}

	/**
	 * Getter : return the service.
	 * 
	 * @return the service
	 */
	public abstract S getService();

	/**
	 * Getter : return the entities.
	 * 
	 * @return the entities
	 */
	public DualListModel<E> getEntities() {
		return entities;
	}

	/**
	 * Setter : affect the entities.
	 * 
	 * @param entities
	 *            the entities
	 */
	public void setEntities(DualListModel<E> entities) {
		this.entities = entities;
	}

	/**
	 * Getter : return the loaded.
	 * 
	 * @return the loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Setter : affect the loaded.
	 * 
	 * @param loaded
	 *            the loaded
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * Load the entities from the database.
	 * <p/>
	 * 
	 */
	public void loadEntities() {
		final List<E> sourceEntities = getService().findAll();
		loadTargetEntities();
		// not display the current entities in the list
		sourceEntities.removeAll(targetEntities);
		entities = new DualListModel<E>(sourceEntities, targetEntities);
		this.loaded = true;
	}

	/**
	 * Load target entities.
	 */
	protected abstract void loadTargetEntities();

	/**
	 * Returns the selected entities.
	 * 
	 * @return the selected
	 */
	public List<E> getSelectedEntities() {
		return entities.getTarget();
	}

	/**
	 * Returns the added entities.
	 * 
	 * @return the added
	 */
	public List<E> getAddedEntities() {
		final List<E> result = new ArrayList<E>(getSelectedEntities());
		result.removeAll(targetEntities);
		return result;
	}

	/**
	 * Returns the removed entities.
	 * 
	 * @return the removed
	 */
	public List<E> getRemovedEntities() {
		final List<E> result = new ArrayList<E>(targetEntities);
		result.removeAll(getSelectedEntities());
		return result;
	}

	/**
	 * Checks for changed.
	 * 
	 * @return true, if successful
	 */
	public boolean hasChanged() {
		return !CollectionUtil.isEmpty(getAddedEntities()) || !CollectionUtil.isEmpty(getRemovedEntities());
	}

	/**
	 * Call after an update in the database to reinit the target entities.
	 */
	public void update() {
		if (targetEntities != null) {
			targetEntities.clear();
			if (entities != null) {
				targetEntities.addAll(entities.getTarget());
			}
		}
	}

	/**
	 * Return the source elements count (ie the non-selected elements count).
	 * 
	 * @return the non-selected elements count
	 */
	public abstract Integer getSourceCount();

	/**
	 * Gets the target count.
	 * 
	 * @return the target count
	 */
	public abstract Integer getTargetCount();
}
