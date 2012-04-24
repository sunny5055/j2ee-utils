package com.google.code.jee.utils.bundle.hibernate.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.code.jee.utils.bundle.hibernate.dao.LabelDao;
import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.SortOrder;
import com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate;

/**
 * The Class LabelDaoImpl.
 */
@Repository
public class LabelDaoImpl extends AbstractGenericDaoHibernate<Integer, Label> implements LabelDao {

    /**
     * Instantiates a new label dao impl.
     */
    public LabelDaoImpl() {
        super(Label.class);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countByKey(String key) {
        return this.getNumberByNamedQueryAndNamedParam(LabelDao.COUNT_BY_KEY, new String[] { "key" }, key);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Label findByKey(String key) {
        return this.getByNamedQueryAndNamedParam(LabelDao.FIND_BY_KEY, new String[] { "key" }, key);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countByLanguage(String language) {
        return this.getNumberByNamedQueryAndNamedParam(LabelDao.COUNT_BY_LANGUAGE, new String[] { "language" },
                language);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Label findByLanguage(String language) {
        return this.getByNamedQueryAndNamedParam(LabelDao.FIND_BY_LANGUAGE, new String[] { "language" }, language);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    protected Search getSearch(SearchCriteria searchCriteria) {
        Search search = null;
        if (searchCriteria != null) {
            search = new Search();

            final StringBuilder buffer = new StringBuilder();
            buffer.append("from Label l ");
            if (searchCriteria.hasFilters()) {
                buffer.append("where ");
                int index = 0;
                for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
                    if (entry.getValue() != null) {
                        if (index != 0) {
                            buffer.append("AND ");
                        }
                        if (entry.getKey().equals("key")) {
                            buffer.append("upper(l.key) like upper(:key) ");
                            search.addStringParameter("key", entry.getValue());
                        } else if (entry.getKey().equals("value")) {
                            buffer.append("upper(l.value) like upper(:value) ");
                            search.addStringParameter("value", entry.getValue());
                        } else if (entry.getKey().equals("language")) {
                            buffer.append("upper(l.language) like upper(:language) ");
                            search.addStringParameter("language", entry.getValue());
                        }
                        index++;
                    }
                }
            }

            search.setCountQuery("select count(*) " + buffer.toString());

            if (searchCriteria.hasSorts()) {
                buffer.append("order by ");
                int index = 0;
                for (final Map.Entry<String, SortOrder> entry : searchCriteria.getSorts().entrySet()) {
                    if (index != 0) {
                        buffer.append(", ");
                    }
                    if (entry.getKey().equals("key")) {
                        buffer.append("l.key ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                            buffer.append("desc ");
                        }
                    } else if (entry.getKey().equals("value")) {
                        buffer.append("l.value ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                            buffer.append("desc ");
                        }
                    }
                    index++;
                }
            }

            search.setQuery(buffer.toString());
        }
        return search;
    }

}
