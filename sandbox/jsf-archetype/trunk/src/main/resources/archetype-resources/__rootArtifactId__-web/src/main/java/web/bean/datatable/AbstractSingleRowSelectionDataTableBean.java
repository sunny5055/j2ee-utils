#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.bean.datatable;

import java.io.Serializable;

import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Class AbstractSingleRowSelectionDataTableBean.
 *
 * @param <PK>
 *            the generic type
 * @param <DTO>
 *            the generic type
 */
@SuppressWarnings("serial")
public abstract class AbstractSingleRowSelectionDataTableBean<PK extends Serializable, DTO extends Dto<PK>> extends AbstractDataTableBean {
	protected DTO selectedObject;

	/**
	 * Instantiates a new abstract single row selection data table bean.
	 */
	public AbstractSingleRowSelectionDataTableBean() {
		super();
	}

	/**
	 * Gets the selected object.
	 *
	 * @return the selected object
	 */
	public DTO getSelectedObject() {
		return selectedObject;
	}

	/**
	 * Sets the selected object.
	 *
	 * @param newSelectedObject
	 *            the new selected object
	 */
	public void setSelectedObject(DTO newSelectedObject) {
		this.selectedObject = newSelectedObject;
	}
}
