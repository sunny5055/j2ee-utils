package com.google.code.jee.utils.mail.hibernate.dao;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.mail.hibernate.model.Mail;

/**
 * The Interface MailDao.
 */
public interface MailDao extends GenericDao<Integer, Mail> {
    String COUNT_BY_NAME = "mail.countByName";
    String FIND_BY_NAME = "mail.findByName";

    /**
     * Search the number of elements with the 'name' parameter.
     * 
     * @param name the name
     * @return the number of element found
     */
    Integer countByName(String name);

    /**
     * Search an element by its name.
     * 
     * @param name the name
     * @return the mail
     */
    Mail findByName(String name);
}
