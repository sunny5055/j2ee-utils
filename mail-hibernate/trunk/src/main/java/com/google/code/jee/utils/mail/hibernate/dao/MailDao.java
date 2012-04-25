package com.google.code.jee.utils.mail.hibernate.dao;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.mail.hibernate.model.Mail;

/**
 * The Interface MailDao.
 */
public interface MailDao extends GenericDao<Integer, Mail> {
    String COUNT_BY_NAME = "mail.countByName";
    String FIND_BY_NAME = "mail.findByName";
    String COUNT_FOR_MAIL_ID_AND_NAME = "mail.countForMailIdAndName";
    String COUNT_FOR_MAIL_ID = "mail.countForMailId";

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

    /**
     * Count the number of attachment with a specific name and corresponding to a specific mail
     * 
     * @param mailId
     * @param attachmentName
     * @return the number of attachments
     */
    Integer countForMailIdAndName(Integer mailId, String attachmentName);

    /**
     * Count the number of attachments of a specific mail
     * 
     * @param mailId the mail id
     * @return the number of attachments
     */
    Integer countForMailId(Integer mailId);
}
