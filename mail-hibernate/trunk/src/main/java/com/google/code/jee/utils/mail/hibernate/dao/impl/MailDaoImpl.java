package com.google.code.jee.utils.mail.hibernate.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.SortOrder;
import com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate;
import com.google.code.jee.utils.mail.hibernate.dao.MailDao;
import com.google.code.jee.utils.mail.hibernate.model.Mail;

/**
 * The Class MailDaoImpl.
 */
@Repository
public class MailDaoImpl extends AbstractGenericDaoHibernate<Integer, Mail> implements MailDao {

    /**
     * Instantiates a new mail dao impl.
     */
    public MailDaoImpl() {
        super(Mail.class);
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
            buffer.append("from Mail m ");
            if (searchCriteria.hasFilters()) {
                buffer.append("where ");
                int index = 0;
                for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
                    if (entry.getValue() != null) {
                        if (index != 0) {
                            buffer.append("AND ");
                        }
                        if (entry.getKey().equals("name")) {
                            buffer.append("upper(m.name) like upper(:name) ");
                            search.addStringParameter("name", entry.getValue());
                            buffer.append("AND ");
                            buffer.append("m.template = true ");
                            search.addStringParameter("template", true);
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
                    if (entry.getKey().equals("name")) {
                        buffer.append("m.name ");
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

    /**
     * {@inheritedDoc}
     */

    @Override
    public Integer countByName(String name) {
        return this.getNumberByNamedQueryAndNamedParam(MailDao.COUNT_BY_NAME, new String[] { "name" }, name);
    }

    /**
     * {@inheritedDoc}
     */

    @Override
    public Mail findByName(String name) {
        return this.getByNamedQueryAndNamedParam(MailDao.FIND_BY_NAME, new String[] { "name" }, name);
    }
}
