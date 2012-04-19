package com.google.code.jee.utils.mail.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.io.IoUtil;
import com.google.code.jee.utils.mail.exception.MailServiceException;
import com.google.code.jee.utils.mail.service.MailService;

/**
 * The Class MailServiceImpl.
 */
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, List<String> recipients, String subject, String text, boolean htmlMessage)
            throws MailServiceException {
        sendMail(from, from, recipients, null, null, subject, text, htmlMessage, null);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, String replyTo, List<String> recipients, String subject, String text,
            boolean htmlMessage) throws MailServiceException {
        sendMail(from, replyTo, recipients, null, null, subject, text, htmlMessage, null);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, List<String> recipients, String subject, String text, boolean htmlMessage,
            List<InputStream> inputStreams) throws MailServiceException {
        sendMail(from, from, recipients, null, null, subject, text, htmlMessage, inputStreams);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, String replyTo, List<String> recipients, String subject, String text,
            boolean htmlMessage, List<InputStream> inputStreams) throws MailServiceException {
        sendMail(from, replyTo, recipients, null, null, subject, text, htmlMessage, inputStreams);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, List<String> recipients, List<String> carbonCopies,
            List<String> blindCarbonCopies, String subject, String text, boolean htmlMessage,
            List<InputStream> inputStreams) throws MailServiceException {
        sendMail(from, from, recipients, carbonCopies, blindCarbonCopies, subject, text, htmlMessage, inputStreams);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void sendMail(String from, String replyTo, List<String> recipients, List<String> carbonCopies,
            List<String> blindCarbonCopies, String subject, String text, boolean htmlMessage,
            List<InputStream> inputStreams) throws MailServiceException {
        if (StringUtil.isBlank(from)) {
            throw new MailServiceException("'From' field is undetermined");
        }
        if (CollectionUtil.isEmpty(recipients) && CollectionUtil.isEmpty(carbonCopies)
                && CollectionUtil.isEmpty(blindCarbonCopies)) {
            throw new MailServiceException("The e-mail cannot be sent without at least one recipient.");
        }

        // E-mail creation
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        if (mimeMessage != null) {
            MimeMessageHelper mail = null;
            try {
                mail = new MimeMessageHelper(mimeMessage, true);
                mail.setFrom(from);
                if (!StringUtil.isBlank(replyTo)) {
                    mail.setReplyTo(from);
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
                    for (final InputStream file : inputStreams) {
                        mail.addAttachment("Attachment_" + i++, new ByteArrayResource(IoUtil.toByteArray(file)));
                    }
                }
            } catch (final MessagingException e) {
                throw new MailServiceException(e);
            } catch (final IOException e) {
                throw new MailServiceException(e);
            }

            mailSender.send(mimeMessage);
        }
    }
}
