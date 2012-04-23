package com.google.code.jee.utils.mail.hibernate.service;

import java.util.List;

import com.google.code.jee.utils.dal.service.GenericService;
import com.google.code.jee.utils.mail.hibernate.model.MailAttachment;

/**
 * The Interface MailAttachmentService.
 */
public interface MailAttachmentService extends GenericService<Integer, MailAttachment> {
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
     * @param code the code
     * @return the mail
     */
    MailAttachment findByName(String name);

    /**
     * Search all the elements and sorts them by name.
     * 
     * @return the list of mails
     */
    List<MailAttachment> findAllByName();
}
