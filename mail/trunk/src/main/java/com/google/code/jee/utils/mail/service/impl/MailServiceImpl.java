package com.google.code.jee.utils.mail.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.mail.exception.MailServiceException;
import com.google.code.jee.utils.mail.service.MailService;

/**
 * The Class MailServiceImpl.
 */
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    public void setMailSender(JavaMailSender sender) {
        this.mailSender = sender;
    }

    /**.
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, List<String> recipients, String subject, 
            String text, boolean htmlMessage) throws MailServiceException {
        sendMail(from, "", recipients, null, null, subject, text, htmlMessage, null);
    }

    /**.
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, String replyTo, List<String> recipients, 
            String subject, String text, boolean htmlMessage) 
                    throws MailServiceException {
        sendMail(from, replyTo, recipients, null, null, subject, text, htmlMessage, null);
    }

    /**.
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, List<String> recipients, String subject, 
            String text, boolean htmlMessage, List<InputStream> inputStreams) 
                    throws MailServiceException {
        sendMail(from, "", recipients, null, null, subject, text, htmlMessage, inputStreams);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, String replyTo, List<String> recipients, 
            String subject, String text, boolean htmlMessage, List<InputStream> inputStreams) 
                    throws MailServiceException {
        sendMail(from, replyTo, recipients, null, null, subject, text, htmlMessage, inputStreams);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, List<String> recipients, 
            List<String> carbonCopies, List<String> blindCarbonCopies, 
            String subject, String text, boolean htmlMessage, 
            List<InputStream> inputStreams) throws MailServiceException {
        sendMail(from, "", recipients, carbonCopies, blindCarbonCopies, subject, text, htmlMessage, inputStreams);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, String replyTo, List<String> recipients, 
            List<String> carbonCopies,List<String> blindCarbonCopies, 
            String subject, String text, boolean htmlMessage,
            List<InputStream> inputStreams) throws MailServiceException {
        // E-mail creation
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mail;
        try {
            mail = new MimeMessageHelper(mimeMessage, true);
        } catch (final MessagingException e1) {
            throw new MailServiceException(e1);
        }

        //
        try {
            if (!StringUtil.isBlank(from)) {
                mail.setFrom(from);
            } else {
                throw new MailServiceException("'From' field is undetermined");
            }
            if (!CollectionUtil.isEmpty(recipients)) {
                mail.setTo(recipients.toArray(new String[recipients.size()]));
            } else {
                throw new MailServiceException("'To' field is undetermined");
            }
            if (!StringUtil.isEmpty(text)) {
                mail.setText(text, htmlMessage);
            } else {
                throw new MailServiceException("The e-mail cannot be sent without a content");
            }
            if (!CollectionUtil.isEmpty(carbonCopies)) {
                mail.setCc(carbonCopies.toArray(new String[carbonCopies.size()]));
            }
            if (!CollectionUtil.isEmpty(blindCarbonCopies)) {
                mail.setBcc(blindCarbonCopies.toArray(new String[blindCarbonCopies.size()]));
            }
        } catch (final MessagingException e) {
            throw new MailServiceException(e);
        }

        try {
            if (!StringUtil.isBlank(replyTo)) {
                mail.setReplyTo(from);
            }

            if (!StringUtil.isEmpty(subject)) {
                mail.setSubject(subject);
            }

            if (!CollectionUtil.isEmpty(inputStreams)) {
                int i = 0;
                for (InputStream file : inputStreams) {
                    try {
                        mail.addAttachment("Attachment_" + i++,
                                new ByteArrayResource(IOUtils.toByteArray(file)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (final MessagingException e) {
            throw new MailServiceException(e);
        }
        mailSender.send(mimeMessage);
    }
}
