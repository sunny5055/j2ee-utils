package com.google.code.jee.utils.mail.impl;

import com.google.code.jee.utils.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.mail.exception.MailServiceException;
import com.google.code.jee.utils.mail.service.MailService;

/**
 * The Class MailServiceImpl.
 */
public class MailServiceImpl implements MailService {

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, List<String> recipients, String subject, String text, boolean htmlMessage)
            throws MailServiceException {
        sendEmail(from, "", recipients, null, null, subject, text, htmlMessage, null);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, String replyTo, List<String> recipients, String subject, String text,
            boolean htmlMessage) throws MailServiceException {
        sendEmail(from, replyTo, recipients, null, null, subject, text, htmlMessage, null);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, List<String> recipients, String subject, String text, boolean htmlMessage,
            List<InputStream> inputStreams) throws MailServiceException {
        sendEmail(from, "", recipients, null, null, subject, text, htmlMessage, inputStreams);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, String replyTo, List<String> recipients, String subject, String text,
            boolean htmlMessage, List<InputStream> inputStreams) throws MailServiceException {
        sendEmail(from, replyTo, recipients, null, null, subject, text, htmlMessage, inputStreams);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, List<String> recipients, List<String> carbonCopies,
            List<String> blindCarbonCopies, String subject, String text, boolean htmlMessage,
            List<InputStream> inputStreams) throws MailServiceException {
        sendEmail(from, "", recipients, carbonCopies, blindCarbonCopies, subject, text, htmlMessage, inputStreams);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, String replyTo, List<String> recipients, List<String> carbonCopies,
            List<String> blindCarbonCopies, String subject, String text, boolean htmlMessage,
            List<InputStream> inputStreams) throws MailServiceException {
        sendEmail(from, replyTo, recipients, carbonCopies, blindCarbonCopies, subject, text, htmlMessage, inputStreams);
    }

    protected void sendEmail(String from, String replyTo, List<String> recipients, List<String> carbonCopies,
            List<String> blindCarbonCopies, String subject, String text, boolean htmlMessage,
            List<InputStream> inputStreams) throws MailServiceException {
        ApplicationContext context = new ClassPathXmlApplicationContext("springMail.xml");

        JavaMailSender sender = (JavaMailSender) context.getBean("mailSender");
        MimeMessage mimeMessage = sender.createMimeMessage();

        MimeMessageHelper mail = new MimeMessageHelper(mimeMessage);

        try {
            if (!StringUtil.isBlank(from)) {
                mail.setFrom(from);
            }
            if (!StringUtil.isBlank(replyTo)) {
                mail.setReplyTo(replyTo);
            }
            if (!CollectionUtil.isEmpty(recipients)) {
                mail.setTo(recipients.toArray(new String[recipients.size()]));
            }
            if (!CollectionUtil.isEmpty(carbonCopies)) {
                mail.setCc(carbonCopies.toArray(new String[carbonCopies.size()]));
            }
            if (!CollectionUtil.isEmpty(blindCarbonCopies)) {
                mail.setBcc(blindCarbonCopies.toArray(new String[blindCarbonCopies.size()]));
            }
            if (!StringUtil.isEmpty(subject)) {
                mail.setSubject(subject);
            }

            if (!StringUtil.isEmpty(text)) {
                mail.setText(text, htmlMessage);
            }

            if (!CollectionUtil.isEmpty(inputStreams)) {
                int i = 0;
                for (InputStream file : inputStreams) {
                    try {
                        mail.addAttachment("Attachment_" + i++, new ByteArrayResource(IOUtils.toByteArray(file)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            mail.setSentDate(new Date());
        } catch (final MessagingException e) {
            throw new MailServiceException(e);
        }
        sender.send(mimeMessage);
    }
}
