package com.googlecode.jutils.jsf.bean.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;

import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericReadService;

/**
 * La classe AbstractListBean.
 * 
 * @param <PK>
 *            le type de la clé primaire de l'élément
 * @param <E>
 *            le type de l'entité
 * @param <S>
 *            le type du service
 */
@SuppressWarnings("serial")
public abstract class AbstractListBean<PK extends Serializable, E extends Dto<PK>, S extends GenericReadService<PK, E>> implements Serializable {
	protected List<E> entities;

	/**
	 * Getter : retourne le service.
	 * 
	 * @return le service
	 */
	protected abstract S getService();

	/**
	 * Initialise le bean.
	 */
	@PostConstruct
	protected void init() {
		loadEntities();
	}

	/**
	 * Getter : retourne le entities.
	 * 
	 * @return le entities
	 */
	public List<E> getEntities() {
		return entities;
	}

	/**
	 * Getter : retourne les options.
	 * 
	 * @return les options
	 */
	public SelectItem[] getOptions() {
		final List<SelectItem> options = new ArrayList<SelectItem>();
		if (!CollectionUtils.isEmpty(entities)) {
			for (final E entity : entities) {
				options.add(toSelectItem(entity));
			}
		}
		return options.toArray(new SelectItem[0]);
	}

	/**
	 * To select item.
	 * 
	 * @param entity
	 *            le entity
	 * @return le select item
	 */
	protected abstract SelectItem toSelectItem(E entity);

	/**
	 * Getter : retourne le row count.
	 * 
	 * @return le row count
	 */
	public Integer getRowCount() {
		return getService().count();
	}

	/**
	 * Charge les entites.
	 */
	protected void loadEntities() {
		entities = getService().findAll();
	}
}
