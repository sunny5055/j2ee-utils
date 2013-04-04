package com.googlecode.jutils.mail.jpa.dao;

import com.googlecode.jutils.dal.dao.GenericDao;
import com.googlecode.jutils.mail.jpa.model.Mail;

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
     * @return the number of elements found
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
