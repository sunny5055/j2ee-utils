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
public abstract class AbstractFormBean<PK extends Serializable, E extends Dto<PK>, S extends GenericService<PK, E>>
        implements Serializable {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormBean.class);
    protected E entity;

    public AbstractFormBean() {
    }

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        entity = getNewEntityInstance();
    }

    public abstract S getService();

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    protected abstract E getNewEntityInstance();

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
        if (entity != null) {
            if (prepareUpdate()) {
                this.getService().update(entity);

                reInit();
            }
        }
    }
}