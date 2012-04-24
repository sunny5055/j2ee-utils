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
    String FIND_MAIL_ATTACHMENT = "mailAttachment.findMailAttachment";
    String FIND_ALL_MAIL_ATTACHMENTS = "mailAttachment.findAllMailAttachments";

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
     * Finds the mail attachment.
     * 
     * @param mailId the mail primary key
     * @param attachmentName the attachment name
     * @return the mail attachment
     */
    MailAttachment findByMailIdAndName(Integer mailId, String attachmentName);

    /**
     * Finds the all mail attachments.
     * 
     * @param mailId the mail primary key
     * @return the list
     */
    List<MailAttachment> findAllByMailId(Integer mailId);

}