package com.google.code.jee.utils.dal.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import com.google.code.jee.utils.BooleanUtil;
import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.dto.Dto;

/**
 * The Class AbstractGenericReadDao.
 * 
 * @param <PK> the generic type
 * @param <E> the element type
 */
public abstract class AbstractGenericReadDao<PK extends Serializable, E extends Dto<PK>> implements
        GenericReadDao<PK, E> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractGenericReadDao.class);
    protected Class<E> entityClass;

    /**
     * Instantiates a new abstract generic read dao.
     * 
     * @param type the type
     */
    public AbstractGenericReadDao(Class<E> type) {
        this.entityClass = type;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<E> getObjects(PK... pks) {
        List<E> entities = new ArrayList<E>();
        if (!ArrayUtils.isEmpty(pks)) {
            entities = getObjects(Arrays.asList(pks));
        }
        return entities;
    }

    /**
     * Gets the first element from list.
     * 
     * @param <T> the generic type
     * @param result the result
     * @return the first element from list
     */
    protected <T> T getFirstElementFromList(List<T> result) {
        T object = null;
        if (!CollectionUtils.isEmpty(result)) {
            object = result.get(0);
        }
        return object;
    }

    /**
     * Gets the integer element from list.
     * 
     * @param result the result
     * @return the integer element from list
     */
    protected Integer getIntegerElementFromList(List<?> result) {
        Integer value = null;
        final Object object = getFirstElementFromList(result);
        if (object != null) {
            if (object instanceof Long) {
                value = ((Long) object).intValue();
            } else if (object instanceof Integer) {
                value = (Integer) object;
            }
        }
        return value;
    }

    /**
     * Gets the boolean element from list.
     * 
     * @param result the result
     * @return the boolean element from list
     */
    protected Boolean getBooleanElementFromList(List<?> result) {
        Boolean value = null;
        final Object object = getFirstElementFromList(result);
        if (object != null) {
            value = BooleanUtil.toBooleanObject(object);
        }
        return value;
    }

    /**
     * Gets the search.
     * 
     * @param searchCriteria the search criteria
     * @return the search
     */
    protected abstract Search getSearch(SearchCriteria searchCriteria);
}
