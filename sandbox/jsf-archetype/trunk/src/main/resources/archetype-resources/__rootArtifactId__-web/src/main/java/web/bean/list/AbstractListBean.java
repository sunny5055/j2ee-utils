#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.bean.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import com.googlecode.jutils.collection.CollectionUtil;

import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericReadService;

/**
 * The Class AbstractListBean.
 *
 * @param <PK> the generic type
 * @param <E> the element type
 * @param <S> the generic type
 */
@SuppressWarnings("serial")
public abstract class AbstractListBean<PK extends Serializable, E extends Dto<PK>, S extends GenericReadService<PK, E>>
        implements Serializable {
    protected List<E> entities;

    protected abstract S getService();

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        loadEntities();
    }

    public List<E> getEntities() {
        return entities;
    }

    public SelectItem[] getOptions() {
        final List<SelectItem> options = new ArrayList<SelectItem>();
        if (!CollectionUtil.isEmpty(entities)) {
            for (final E entity : entities) {
                options.add(toSelectItem(entity));
            }
        }
        return options.toArray(new SelectItem[0]);
    }

    /**
     * To select item.
     *
     * @param entity the entity
     * @return the select item
     */
    protected abstract SelectItem toSelectItem(E entity);

    public Integer getRowCount() {
        return getService().count();
    }

    /**
     * Load entities.
     */
    protected void loadEntities() {
        entities = getService().findAll();
    }
}
