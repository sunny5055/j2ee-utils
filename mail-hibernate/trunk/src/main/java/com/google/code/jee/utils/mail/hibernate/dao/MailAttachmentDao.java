package com.google.code.jee.utils.mail.hibernate.dao;

import java.util.List;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.mail.hibernate.model.MailAttachment;

/**
 * The Interface MailAttachmentDao.
 */
public interface MailAttachmentDao extends GenericDao<Integer, MailAttachment> {
    
    String COUNT_BY_NAME = "mailAttachment.countByName";
    String FIND_BY_NAME = "mailAttachment.findByName";
    String FIND_ALL_BY_NAME = "mailAttachment.findAllByName";

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
    MailAttachment findByName(String name);

    /**
     * Search all the elements and sorts them by name.
     *
     * @return the list of attachments
     */
    List<MailAttachment> findAllByName();

}