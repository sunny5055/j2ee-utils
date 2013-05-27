#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.bean.form;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jutils.BooleanUtil;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericService;


@SuppressWarnings("serial")
public abstract class AbstractFormBean<PK extends Serializable, DTO extends Dto<PK>, S extends GenericService<PK, DTO>>
        implements Serializable {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormBean.class);
    protected DTO model;
    protected boolean editionMode;

    public AbstractFormBean() {
    }

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
    	model = getFromRequest();

    	final FacesContext facesContext = FacesContext.getCurrentInstance();
        Object parameter = null;
        //parameter = FacesUtils.getCustomScopeParameter(facesContext, "editionMode");
        if (parameter != null && BooleanUtil.toBooleanObject(parameter)) {
            editionMode = true;
        }

        if (model == null) {
            model = getNewInstance();
        }
    }

    public abstract S getService();

    public DTO getModel() {
        return model;
    }

    public void setModel(DTO model) {
        this.model = model;
    }

    public abstract PK getPrimaryKey();

    protected abstract DTO getNewInstance();

    protected abstract String getListPage();

    public boolean isEditionMode() {
        return editionMode;
    }

    public void setEditionMode(boolean editionMode) {
        this.editionMode = editionMode;
    }

    public DTO getFromRequest() {
        DTO dto = null;
        final PK pk = getPrimaryKey();
        if (pk != null) {
            dto = this.getService().get(pk);
        }
        return dto;
    }

    protected String getViewPage() {
        return "";
    }

    /**
     * Re init.
     */
    protected void reInit() {
    	 if (model != null && model.getPrimaryKey() != null) {
             model = getService().get(model.getPrimaryKey());
         } else {
             model = getNewInstance();
         }

         setEditionMode(false);
    }

    /**
     * Re init.
     *
     * @param event the event
     */
    public void reInit(ActionEvent event) {
        this.reInit();
    }

    protected abstract boolean prepareUpdate();

    public void update(ActionEvent event) {
        if (model != null) {
            if (prepareUpdate()) {
                this.getService().update(model);

                reInit();
            }
        }
    }

    public void editionMode(ActionEvent event) {
        setEditionMode(true);
    }
}