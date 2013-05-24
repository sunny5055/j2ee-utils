#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.bean.form;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericService;

@SuppressWarnings("serial")
public abstract class AbstractFormBean<PK extends Serializable, DTO extends Dto<PK>, S extends GenericService<PK, DTO>>
        implements Serializable {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormBean.class);
    protected DTO model;

    public AbstractFormBean() {
    }

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        model = getNewModelInstance();
    }

    public abstract S getService();

    public DTO getModel() {
        return model;
    }

    public void setModel(DTO model) {
        this.model = model;
    }

    protected abstract DTO getNewModelInstance();

    protected abstract String getListPage();

    protected String getViewPage() {
        return "";
    }

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

    protected abstract boolean prepareUpdate();

    public void update(ActionEvent event) {
        if (model != null) {
            if (prepareUpdate()) {
                this.getService().update(model);

                reInit();
            }
        }
    }
}