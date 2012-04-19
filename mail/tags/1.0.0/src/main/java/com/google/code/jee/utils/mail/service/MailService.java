package com.google.code.jee.utils.mail.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.google.code.jee.utils.mail.exception.MailServiceException;

/**
 * The Interface MailService.
 */
public interface MailService {

    /**
     * Send an email.
     * 
     * @param from the email sender
     * @param recipients list that contains the email's recipients
     * @param subject the email subject
     * @param text the email message
     * @param htmlMessage the html message
     * @throws MailServiceException the mail service exception
     */
    void sendMail(String from, List<String> recipients, String subject, String text, boolean htmlMessage)
            throws MailServiceException;

    /**
     * Send an email.
     * 
     * @param from the email sender
     * @param replyTo the list of the senders
     * @param recipients list that contains the email's recipients
     * @param subject the email subject
     * @param text the email message
     * @param htmlMessage the html message
     * @throws MailServiceException the mail service exception
     */
    void sendMail(String from, String replyTo, List<String> recipients, String subject, String text, boolean htmlMessage)
            throws MailServiceException;

    /**
     * Send an email.
     * 
     * @param from the email sender
     * @param recipients list that contains the email's recipients
     * @param subject the email subject
     * @param text the email message
     * @param htmlMessage the html message
     * @param attachments list that contains the email's attachments
     * @throws MailServiceException the mail service exception
     */
    void sendMail(String from, List<String> recipients, String subject, String text, boolean htmlMessage,
            Map<String, InputStream> attachments) throws MailServiceException;

    /**
     * Send an email.
     * 
     * @param from the email sender
     * @param replyTo the list of the senders
     * @param recipients list that contains the email's recipients
     * @param subject the email subject
     * @param text the email message
     * @param htmlMessage the html message
     * @param attachments list that contains the email's attachments
     * @throws MailServiceException the mail service exception
     */
    void sendMail(String from, String replyTo, List<String> recipients, String subject, String text,
            boolean htmlMessage, Map<String, InputStream> attachments) throws MailServiceException;

    /**
     * Send an email.
     * 
     * @param from the email sender
     * @param recipients list that contains the email's recipients
     * @param carbonCopies list that contains all the carbon copies recipients
     * @param blindCarbonCopies list that contains all the blind carbon copies
     *            recipients
     * @param subject the email subject
     * @param text the email message
     * @param htmlMessage the html message
     * @param attachments list that contains the email's attachments
     * @throws MailServiceException the mail service exception
     */
    void sendMail(String from, List<String> recipients, List<String> carbonCopies, List<String> blindCarbonCopies,
            String subject, String text, boolean htmlMessage, Map<String, InputStream> attachments)
            throws MailServiceException;

    /**
     * Send an email.
     * 
     * @param from the email sender
     * @param replyTo the list of the senders
     * @param recipients list that contains the email's recipients
     * @param carbonCopies list that contains all the carbon copies recipients
     * @param blindCarbonCopies list that contains all the blind carbon copies
     *            recipients
     * @param subject the email subject
     * @param text the email message
     * @param htmlMessage the html message
     * @param attachments list that contains the email's attachments
     * @throws MailServiceException the mail service exception
     */
    void sendMail(String from, String replyTo, List<String> recipients, List<String> carbonCopies,
            List<String> blindCarbonCopies, String subject, String text, boolean htmlMessage,
            Map<String, InputStream> attachments) throws MailServiceException;

}
