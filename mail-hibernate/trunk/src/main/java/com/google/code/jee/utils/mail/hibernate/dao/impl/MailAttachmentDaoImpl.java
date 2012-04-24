package com.google.code.jee.utils.mail.hibernate.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.SortOrder;
import com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate;
import com.google.code.jee.utils.mail.hibernate.dao.MailAttachmentDao;
import com.google.code.jee.utils.mail.hibernate.model.MailAttachment;

/**
 * The Class MailAttachmentDaoImpl.
 */
@Repository
public class MailAttachmentDaoImpl extends AbstractGenericDaoHibernate<Integer, MailAttachment> implements
        MailAttachmentDao {

    /**
     * Instantiates a new mail attachment dao impl.
     */
    public MailAttachmentDaoImpl() {
        super(MailAttachment.class);
    }

    /**
     * {@inheritedDoc}
     */

    @Override
    public Integer countByName(String name) {
        return this.getNumberByNamedQueryAndNamedParam(MailAttachmentDao.COUNT_BY_NAME, new String[] { "name" }, name);
    }

    /**
     * {@inheritedDoc}
     */

    @Override
    public MailAttachment findByName(String name) {
        return this.getByNamedQueryAndNamedParam(MailAttachmentDao.FIND_BY_NAME, new String[] { "name" }, name);

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
            buffer.append("from MailAttachment maa, Mail mai ");
            if (searchCriteria.hasFilters()) {
                buffer.append("where ");
                int index = 0;
                for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
                    if (entry.getValue() != null) {
                        if (index != 0) {
                            buffer.append("AND ");
                        }
                        if (entry.getKey().equals("mailPrimaryKey")) {
                            buffer.append("mai.id = mailPrimaryKey ");
                            search.addStringParameter("mailPrimaryKey", entry.getValue());
                        } else if (entry.getKey().equals("attachmentName")) {
                            buffer.append("upper(maa.attachmentName) like upper(:attachmentName) ");
                            search.addStringParameter("attachmentName", entry.getValue());
                        } else if (entry.getKey().equals("name")) {
                            buffer.append("upper(maa.name) like upper(:name) ");
                            search.addStringParameter("name", entry.getValue());
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
                        buffer.append("maa.name ");
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
    public MailAttachment findByMailIdAndName(Integer mailPrimaryKey, String attachmentName) {
        return this.getByNamedQueryAndNamedParam(MailAttachmentDao.FIND_BY_MAIL_ID_AND_NAME, new String[] {
                "mailPrimaryKey", "attachmentName" }, mailPrimaryKey, attachmentName);
    }

    /**
     * {@inheritedDoc}
     */

    @Override
    public List<MailAttachment> findAllByMailId(Integer mailPrimaryKey) {
        return this.findByNamedQueryAndNamedParam(MailAttachmentDao.FIND_ALL_BY_MAIL_ID,
                new String[] { "mailPrimaryKey" }, mailPrimaryKey);
    }

}
