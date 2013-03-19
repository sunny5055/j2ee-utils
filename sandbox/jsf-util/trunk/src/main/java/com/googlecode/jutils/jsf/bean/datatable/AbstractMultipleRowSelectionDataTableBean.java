package com.googlecode.jutils.jsf.bean.datatable;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Class MultipleRowSelectionDataTableBean.
 * 
 * @param <PK>
 *            the entity primary key type
 * @param <E>
 *            the entity type
 * @param <S>
 *            the service type
 */
@SuppressWarnings("serial")
public abstract class AbstractMultipleRowSelectionDataTableBean<PK extends Serializable, E extends Dto<PK>> extends AbstractDataTableBean {
	protected E[] selectedObjects;

	/**
	 * Getter : return the selectedObjects
	 * 
	 * @return the selectedObjects
	 */
	public E[] getSelectedObjects() {
		return selectedObjects;
	}

	/**
	 * Setter : affect the selectedObjects
	 * 
	 * @param selectedObjects
	 *            the selectedObjects
	 */
	public void setSelectedObjects(E[] selectedObjects) {
		this.selectedObjects = selectedObjects;
	}

	/**
	 * Gets the selection count.
	 * 
	 * @return the selection count
	 */
	public Integer getSelectionCount() {
		Integer count = 0;
		if (!ArrayUtil.isEmpty(selectedObjects)) {
			count = selectedObjects.length;
		}
		return count;
	}

	/**
	 * Gets the selected objects as list.
	 * 
	 * @return the selected objects as list
	 */
	@SuppressWarnings("unchecked")
	public Set<E> getSelectedObjectsAsSet() {
		final Set<E> list = new LinkedHashSet<E>();
		if (!ArrayUtil.isEmpty(selectedObjects)) {
			for (final Dto<PK> dto : selectedObjects) {
				list.add((E) dto);
			}
		}
		return list;
	}

	/**
	 * Clear selection.
	 */
	public void clearSelection() {
		this.selectedObjects = null;
	}
}
