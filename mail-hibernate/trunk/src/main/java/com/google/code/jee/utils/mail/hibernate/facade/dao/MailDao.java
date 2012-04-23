package com.google.code.jee.utils.mail.hibernate.facade.dao;

import java.util.List;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.mail.hibernate.facade.model.Mail;
import com.google.code.jee.utils.mail.hibernate.facade.model.MailAttachment;

/**
 * The Interface MailDao.
 */
public interface MailDao extends GenericDao<Integer, Mail> {
    String COUNT_BY_NAME = "mail.countByName";
    String FIND_BY_NAME = "mail.findByName";
    String FIND_ALL_BY_NAME = "mail.findAllByName";
    String COUNT_BY_SUBJECT = "mail.countBySubject";
    String FIND_BY_SUBJECT = "mail.findBySubject";
    String FIND_ALL_BY_SUBJECT = "mail.findAllBySubject";
    //String FIND_ALL_ATTACHMENTS_BY_NAME = "mail.findAllAttachmentsByName";

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
     * Search all the elements and sorts them by name.
     *
     * @return the list of mails
     */
    List<Mail> findAllByName();
    
    /**
     * Search the number of elements with the 'subject' parameter.
     *
     * @param subject the subject
     * @return the number of element found
     */
    Integer countBySubject(String subject);

    /**
     * Search an element by its subject.
     *
     * @param subject the subject
     * @return the mail
     */
    Mail findBySubject(String subject);

    /**
     * Search all the elements and sorts them by subject.
     *
     * @return the list of mails
     */
    List<Mail> findAllBySubject();
    
    /**
     * Search all the elements and sorts them by name.
     *
     * @return the list of attachments
     */
    //List<MailAttachment> findAllAttachmentsByName();
}
