#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.bean.datatable;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Class AbstractMultipleRowSelectionDataTableBean.
 *
 * @param <PK>
 *            the generic type
 * @param <DTO>
 *            the generic type
 */
@SuppressWarnings("serial")
public abstract class AbstractMultipleRowSelectionDataTableBean<PK extends Serializable, DTO extends Dto<PK>> extends AbstractDataTableBean {
	protected DTO[] selectedObjects;

	/**
	 * Instantiates a new abstract multiple row selection data table bean.
	 */
	public AbstractMultipleRowSelectionDataTableBean() {
		super();
	}

	/**
	 * Getter : return the selectedObjects
	 *
	 * @return the selectedObjects
	 */
	public DTO[] getSelectedObjects() {
		return selectedObjects;
	}

	/**
	 * Setter : affect the selectedObjects
	 *
	 * @param selectedObjects
	 *            the selectedObjects
	 */
	public void setSelectedObjects(DTO[] selectedObjects) {
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
	public Set<DTO> getSelectedObjectsAsSet() {
		final Set<DTO> list = new LinkedHashSet<DTO>();
		if (!ArrayUtil.isEmpty(selectedObjects)) {
			for (final DTO dto : selectedObjects) {
				list.add(dto);
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
