package com.googlecode.jutils.mail.service.impl;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.mail.exception.MailSenderServiceException;
import com.googlecode.jutils.mail.service.MailSenderService;

/**
 * The Class MailServiceImpl.
 */
@Service
public class MailSenderServiceImpl implements MailSenderService {
	protected static final Logger LOGGER = LoggerFactory.getLogger(MailSenderServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void sendMail(String from, List<String> recipients, String subject, String text, boolean htmlMessage) throws MailSenderServiceException {
		sendMail(from, from, recipients, null, null, subject, text, htmlMessage, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void sendMail(String from, String replyTo, List<String> recipients, String subject, String text, boolean htmlMessage) throws MailSenderServiceException {
		sendMail(from, replyTo, recipients, null, null, subject, text, htmlMessage, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void sendMail(String from, List<String> recipients, String subject, String text, boolean htmlMessage, Map<String, byte[]> attachments) throws MailSenderServiceException {
		sendMail(from, from, recipients, null, null, subject, text, htmlMessage, attachments);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void sendMail(String from, String replyTo, List<String> recipients, String subject, String text, boolean htmlMessage, Map<String, byte[]> attachments)
			throws MailSenderServiceException {
		sendMail(from, replyTo, recipients, null, null, subject, text, htmlMessage, attachments);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void sendMail(String from, List<String> recipients, List<String> carbonCopies, List<String> blindCarbonCopies, String subject, String text, boolean htmlMessage,
			Map<String, byte[]> attachments) throws MailSenderServiceException {
		sendMail(from, from, recipients, carbonCopies, blindCarbonCopies, subject, text, htmlMessage, attachments);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void sendMail(String from, String replyTo, List<String> recipients, List<String> carbonCopies, List<String> blindCarbonCopies, String subject, String text,
			boolean htmlMessage, Map<String, byte[]> attachments) throws MailSenderServiceException {
		if (StringUtil.isBlank(from)) {
			throw new MailSenderServiceException("'From' field is undetermined");
		}
		if (StringUtil.isBlank(text)) {
			throw new MailSenderServiceException("The e-mail cannot be sent without content");
		}
		if (CollectionUtil.isEmpty(recipients) && CollectionUtil.isEmpty(carbonCopies) && CollectionUtil.isEmpty(blindCarbonCopies)) {
			throw new MailSenderServiceException("The e-mail cannot be sent without at least one recipient.");
		}

		// E-mail creation
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		if (mimeMessage != null) {
			MimeMessageHelper mail = null;
			try {
				mail = new MimeMessageHelper(mimeMessage, true);
				mail.setFrom(from);
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

				mail.setText(text, htmlMessage);

				if (!MapUtil.isEmpty(attachments)) {
					for (final String fileName : attachments.keySet()) {
						mail.addAttachment(fileName, new ByteArrayResource(attachments.get(fileName)));
					}
				}
			} catch (final MessagingException e) {
				throw new MailSenderServiceException(e);
			}

			mailSender.send(mimeMessage);
		}
	}
}
