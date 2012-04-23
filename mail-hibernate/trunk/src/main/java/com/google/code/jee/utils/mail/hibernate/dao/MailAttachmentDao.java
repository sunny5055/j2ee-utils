package com.google.code.jee.utils.mail.hibernate.dao;

import java.util.List;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.mail.hibernate.model.MailAttachment;

/**
 * The Interface MailAttachmentDao.
 */
public interface MailAttachmentDao extends GenericDao<Integer, MailAttachment> {
    
    /** The COUN t_ b y_ name. */
    String COUNT_BY_NAME = "mailAttachment.countByName";
    
    /** The FIN d_ b y_ name. */
    String FIND_BY_NAME = "mailAttachment.findByName";
    
    /** The FIN d_ al l_ b y_ name. */
    String FIND_ALL_BY_NAME = "mailAttachment.findAllByName";
    
    /** The FIN d_ al l_ mai l_ attachments. */
    String FIND_ALL_MAIL_ATTACHMENTS = "mailAttachment.findAllMailAttachments";
    
    /** The FIN d_ mai l_ attachment. */
    String FIND_MAIL_ATTACHMENT = "mailAttachment.findMailAttachment";

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

    /**
     * Finds the mail attachment.
     * 
     * @param mailPrimaryKey the mail primary key
     * @param attachmentName the attachment name
     * @return the mail attachment
     */
    MailAttachment findMailAttachment(Integer mailPrimaryKey, String attachmentName);

    /**
     * Finds the all mail attachments.
     * 
     * @param mailPrimaryKey the mail primary key
     * @return the list
     */
    List<MailAttachment> findAllMailAttachments(Integer mailPrimaryKey);

}