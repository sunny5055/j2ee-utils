package com.google.code.jee.utils.mail.hibernate.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.SortOrder;
import com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate;
import com.google.code.jee.utils.mail.hibernate.dao.MailAttachmentDao;
import com.google.code.jee.utils.mail.hibernate.dao.MailDao;
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

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate#create(
     * com.google.code.jee.utils.dal.dto.Dto)
     */
    @Override
    public Integer create(MailAttachment mailAttachment) {
        return super.create(mailAttachment);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate#delete(
     * com.google.code.jee.utils.dal.dto.Dto)
     */
    @Override
    public Integer delete(MailAttachment mailAttachment) {
        return super.delete(mailAttachment);
    }

    /*
     * (non-Javadoc)
     * @see com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate#
     * deleteByPrimaryKey(java.io.Serializable)
     */
    @Override
    public Integer deleteByPrimaryKey(Integer primaryKey) {
        return super.deleteByPrimaryKey(primaryKey);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate#update(
     * com.google.code.jee.utils.dal.dto.Dto)
     */
    @Override
    public Integer update(MailAttachment mailAttachment) {
        return super.update(mailAttachment);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#count()
     */
    @Override
    public Integer count() {
        return super.count();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#count
     * (com.google.code.jee.utils.dal.SearchCriteria)
     */
    @Override
    public Integer count(SearchCriteria searchCriteria) {
        return super.count(searchCriteria);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#existPk
     * (java.io.Serializable)
     */
    @Override
    public boolean existPk(Integer primaryKey) {
        return super.existPk(primaryKey);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#findAll
     * ()
     */
    @Override
    public List<MailAttachment> findAll() {
        return super.findAll();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#findAll
     * (com.google.code.jee.utils.dal.SearchCriteria)
     */
    @Override
    public List<MailAttachment> findAll(SearchCriteria searchCriteria) {
        return super.findAll(searchCriteria);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#get
     * (java.io.Serializable)
     */
    @Override
    public MailAttachment get(Integer primaryKey) {
        return super.get(primaryKey);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#getObjects
     * (PK[])
     */
    @Override
    public List<MailAttachment> getObjects(Integer... primaryKeys) {
        return super.getObjects(primaryKeys);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#getObjects
     * (java.util.Collection)
     */
    @Override
    public List<MailAttachment> getObjects(Collection<Integer> primaryKeys) {
        return super.getObjects(primaryKeys);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.mail_hibernate.facade.dao.MailAttachmentDao
     * #countByName(java.lang.String)
     */
    @Override
    public Integer countByName(String name) {
        return this.getNumberByNamedQueryAndNamedParam(MailAttachmentDao.COUNT_BY_NAME, new String[] { "name" }, name);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.mail_hibernate.facade.dao.MailAttachmentDao
     * #findByName(java.lang.String)
     */
    @Override
    public MailAttachment findByName(String name) {
        return this.getByNamedQueryAndNamedParam(MailDao.FIND_BY_NAME, new String[] { "name" }, name);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.mail_hibernate.facade.dao.MailAttachmentDao
     * #findAllByName()
     */
    @Override
    public List<MailAttachment> findAllByName() {
        return this.findByNamedQuery(MailDao.FIND_ALL_BY_NAME);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDao#getSearch(com
     * .google.code.jee.utils.dal.SearchCriteria)
     */
    @Override
    protected Search getSearch(SearchCriteria searchCriteria) {
        Search search = null;
        if (searchCriteria != null) {
            search = new Search();

            final StringBuilder buffer = new StringBuilder();
            buffer.append("from MailAttachment m ");
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
}
