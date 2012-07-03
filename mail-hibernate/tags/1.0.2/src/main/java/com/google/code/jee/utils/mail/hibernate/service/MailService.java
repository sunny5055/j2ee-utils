package com.google.code.jee.utils.mail.hibernate.service;

import java.util.List;

import com.google.code.jee.utils.dal.service.GenericService;
import com.google.code.jee.utils.mail.hibernate.model.Mail;
import com.google.code.jee.utils.mail.hibernate.model.MailAttachment;

/**
 * The Interface MailService.
 */
public interface MailService extends GenericService<Integer, Mail> {
    /**
     * Test the existence of an element with the parameter 'name'.
     * 
     * @param name the name
     * @return true, if success
     */
    boolean existWithName(String name);

    /**
     * Search an element by its name.
     * 
     * @param name the name
     * @return the mail
     */
    Mail findByName(String name);

    /**
     * Test the existence of an attachment with the parameter 'name' and its
     * mail id.
     * 
     * @param mailId the mailId
     * @param attachmentName the attachment name
     * @return true, if success
     */
    boolean existAttachmentWithName(Integer mailId, String attachmentName);

    /**
     * Search a mail attachment with its name and its mail primary key.
     * 
     * @param mailId the mailId
     * @param attachmentName the attachment name
     * @return the mail attachment
     */
    MailAttachment findAttachmentByName(Integer mailId, String attachmentName);

    /**
     * Count a mail numbers of attachments
     * 
     * @param mailId the mailId
     * @return the number of attachments corresponding to the mail id
     */
    Integer countAttachmentForMailId(Integer mailId);

    /**
     * Search all the mail's attachments.
     * 
     * @param mailPrimaryKey the mail primary key
     * @return the list
     */
    List<MailAttachment> findAllAttachmentsByMailId(Integer mailId);

}
