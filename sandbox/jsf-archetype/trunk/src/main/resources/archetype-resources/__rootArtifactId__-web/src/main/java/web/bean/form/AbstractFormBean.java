#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.bean.form;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jutils.BooleanUtil;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericService;
import ${package}.web.util.FacesUtils;

@SuppressWarnings("serial")
public abstract class AbstractFormBean<PK extends Serializable, DTO extends Dto<PK>, S extends GenericService<PK, DTO>>
        implements Serializable {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormBean.class);
    protected DTO model;
    protected boolean editionMode;

    public AbstractFormBean() {
    }

    @PostConstruct
    protected void init() {
    	model = getFromFlashScope();

        Object parameter = FacesUtils.getFlashAttribute("editionMode");
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

    public boolean isEditionMode() {
        return editionMode;
    }

    public void setEditionMode(boolean editionMode) {
        this.editionMode = editionMode;
    }

    public DTO getFromFlashScope() {
        DTO dto = null;
        final PK pk = getPrimaryKey();
        if (pk != null) {
            dto = this.getService().get(pk);
        }
        return dto;
    }

    protected void reInit() {
    	 if (model != null && model.getPrimaryKey() != null) {
             model = getService().get(model.getPrimaryKey());
         } else {
             model = getNewInstance();
         }

         setEditionMode(false);
    }

    public void reInit(ActionEvent event) {
        this.reInit();
    }

    public void editionMode(ActionEvent event) {
        setEditionMode(true);
    }
}