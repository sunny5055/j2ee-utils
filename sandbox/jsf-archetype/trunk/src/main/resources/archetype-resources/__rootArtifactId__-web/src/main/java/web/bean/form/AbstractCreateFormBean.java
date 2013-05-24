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

/**
 * The Class AbstractCreateFormBean.
 *
 * @param <PK> the generic type
 * @param <DTO> the element type
 * @param <S> the generic type
 */
@SuppressWarnings("serial")
public abstract class AbstractCreateFormBean<PK extends Serializable, DTO extends Dto<PK>, S extends GenericService<PK, DTO>>
        implements Serializable {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCreateFormBean.class);
    protected DTO model;

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

    /**
     * Prepare create.
     *
     * @return true, if successful
     */
    protected abstract boolean prepareCreate();

    /**
     * Creates the model.
     *
     * @param event the event
     */
    public void create(ActionEvent event) {
        if (model != null) {
            if (prepareCreate()) {
                this.getService().create(model);

                reInit();
            }
        }
    }
}