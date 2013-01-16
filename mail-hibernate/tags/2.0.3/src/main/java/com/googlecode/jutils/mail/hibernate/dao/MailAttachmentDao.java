package com.googlecode.jutils.mail.hibernate.dao;

import java.util.List;

import com.googlecode.jutils.dal.dao.GenericDao;
import com.googlecode.jutils.mail.hibernate.model.MailAttachment;

/**
 * The Interface MailAttachmentDao.
 */
public interface MailAttachmentDao extends GenericDao<Integer, MailAttachment> {
    String COUNT_BY_NAME = "mailAttachment.countByName";
    String FIND_BY_NAME = "mailAttachment.findByName";
    String COUNT_FOR_MAIL_ID = "mailAttachment.countForMailId";
    String FIND_ALL_BY_MAIL_ID = "mailAttachment.findAllByMailId";
    String COUNT_FOR_MAIL_ID_AND_NAME = "mailAttachment.countForMailIdAndName";
    String FIND_BY_MAIL_ID_AND_NAME = "mailAttachment.findByMailIdAndName";

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
     * Count the number of attachments of a specific mail
     * 
     * @param mailId the mail id
     * @return the number of attachments
     */
    Integer countForMailId(Integer mailId);

    /**
     * Finds the all mail attachments.
     * 
     * @param mailId the mail primary key
     * @return the list
     */
    List<MailAttachment> findAllByMailId(Integer mailId);

    /**
     * Count the number of attachment with a specific name and corresponding to
     * a specific mail
     * 
     * @param mailId the mailId
     * @param attachmentName the attachment name
     * @return the number of attachments
     */
    Integer countForMailIdAndName(Integer mailId, String attachmentName);

    /**
     * Finds the mail attachment.
     * 
     * @param mailId the mail primary key
     * @param attachmentName the attachment name
     * @return the mail attachment
     */
    MailAttachment findByMailIdAndName(Integer mailId, String attachmentName);

}