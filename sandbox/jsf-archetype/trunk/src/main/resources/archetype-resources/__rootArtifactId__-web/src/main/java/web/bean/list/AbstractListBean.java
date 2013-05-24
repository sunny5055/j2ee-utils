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
 * @param <DTO> the element type
 * @param <S> the generic type
 */
@SuppressWarnings("serial")
public abstract class AbstractListBean<PK extends Serializable, DTO extends Dto<PK>, S extends GenericReadService<PK, DTO>>
        implements Serializable {
    protected List<DTO> list;

    protected abstract S getService();

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        load();
    }

    public List<DTO> getList() {
        return list;
    }

    public SelectItem[] getOptions() {
        final List<SelectItem> options = new ArrayList<SelectItem>();
        if (!CollectionUtil.isEmpty(list)) {
            for (final DTO dto : list) {
                options.add(toSelectItem(dto));
            }
        }
        return options.toArray(new SelectItem[0]);
    }

    /**
     * To select item.
     *
     * @param dto the dto
     * @return the select item
     */
    protected abstract SelectItem toSelectItem(DTO dto);

    public Integer getRowCount() {
        return getService().count();
    }

    /**
     * Load list.
     */
    protected void load() {
        list = getService().findAll();
    }
}
