package com.google.code.jee.utils.mail_hibernate.impl.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.SortOrder;
import com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate;
import com.google.code.jee.utils.mail_hibernate.facade.dao.MailDao;
import com.google.code.jee.utils.mail_hibernate.facade.model.Mail;
import com.google.code.jee.utils.mail_hibernate.facade.model.MailAttachment;

// TODO: Auto-generated Javadoc
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

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate#create(
     * com.google.code.jee.utils.dal.dto.Dto)
     */
    @Override
    public Integer create(Mail mail) {
        return super.create(mail);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate#delete(
     * com.google.code.jee.utils.dal.dto.Dto)
     */
    @Override
    public Integer delete(Mail mail) {
        return super.delete(mail);
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
    public Integer update(Mail mail) {
        return super.update(mail);
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
     * @see com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#
     * existprimaryKey(java.io.Serializable)
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
    public List<Mail> findAll() {
        return super.findAll();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#findAll
     * (com.google.code.jee.utils.dal.SearchCriteria)
     */
    @Override
    public List<Mail> findAll(SearchCriteria searchCriteria) {
        return super.findAll(searchCriteria);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#get
     * (java.io.Serializable)
     */
    @Override
    public Mail get(Integer primaryKey) {
        return super.get(primaryKey);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#getObjects
     * (primaryKey[])
     */
    @Override
    public List<Mail> getObjects(Integer... primaryKeys) {
        return super.getObjects(primaryKeys);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.dao.AbstractGenericReadDaoHibernate#getObjects
     * (java.util.Collection)
     */
    @Override
    public List<Mail> getObjects(Collection<Integer> primaryKeys) {
        return super.getObjects(primaryKeys);
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
                        } else if (entry.getKey().equals("subject")) {
                            buffer.append("upper(m.subject) like upper(:subject) ");
                            search.addStringParameter("subject", entry.getValue());
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
                    } else if (entry.getKey().equals("subject")) {
                        buffer.append("m.subject ");
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

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.mail_hibernate.facade.dao.MailDao#countByName
     * (java.lang.String)
     */
    @Override
    public Integer countByName(String name) {
        return this.getNumberByNamedQueryAndNamedParam(MailDao.COUNT_BY_NAME, new String[] { "name" }, name);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.mail_hibernate.facade.dao.MailDao#findByName
     * (java.lang.String)
     */
    @Override
    public Mail findByName(String name) {
        return this.getByNamedQueryAndNamedParam(MailDao.FIND_BY_NAME, new String[] { "name" }, name);
    }

    /*
     * (non-Javadoc)
     * @see com.google.code.jee.utils.mail_hibernate.facade.dao.MailDao#
     * findAllOrderByName()
     */
    @Override
    public List<Mail> findAllByName() {
        return this.findByNamedQuery(MailDao.FIND_ALL_BY_NAME);
    }

    /* (non-Javadoc)
     * @see com.google.code.jee.utils.mail_hibernate.facade.dao.MailDao#countBySubject(java.lang.String)
     */
    @Override
    public Integer countBySubject(String subject) {
        return this.getNumberByNamedQueryAndNamedParam(MailDao.COUNT_BY_SUBJECT, new String[] { "subject" }, subject);
    }

    /* (non-Javadoc)
     * @see com.google.code.jee.utils.mail_hibernate.facade.dao.MailDao#findBySubject(java.lang.String)
     */
    @Override
    public Mail findBySubject(String subject) {
        return this.getByNamedQueryAndNamedParam(MailDao.FIND_BY_SUBJECT, new String[] { "subject" }, subject);

    }

    /* (non-Javadoc)
     * @see com.google.code.jee.utils.mail_hibernate.facade.dao.MailDao#findAllBySubject()
     */
    @Override
    public List<Mail> findAllBySubject() {
        return this.findByNamedQuery(MailDao.FIND_ALL_BY_SUBJECT);
    }
}
