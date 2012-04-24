package com.google.code.jee.utils.mail.hibernate.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.service.AbstractGenericService;
import com.google.code.jee.utils.mail.hibernate.dao.MailAttachmentDao;
import com.google.code.jee.utils.mail.hibernate.dao.MailDao;
import com.google.code.jee.utils.mail.hibernate.model.Mail;
import com.google.code.jee.utils.mail.hibernate.model.MailAttachment;
import com.google.code.jee.utils.mail.hibernate.service.MailService;

/**
 * The Class MailServiceImpl.
 */
@Service
public class MailServiceImpl extends AbstractGenericService<Integer, Mail, MailDao> implements MailService {
    @Autowired
    private MailAttachmentDao mailAttachmentDao;

    /**
     * {@inheritedDoc}
     */
    @Autowired
    @Override
    public void setDao(MailDao mailDao) {
        this.dao = mailDao;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithName(String name) {
        boolean exist = false;
        if (!StringUtil.isEmpty(name)) {
            final Integer count = dao.countByName(name);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Mail findByName(String name) {
        Mail mail = null;
        if (!StringUtil.isEmpty(name)) {
            mail = dao.findByName(name);
        }
        return mail;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public MailAttachment findByMailIdAndName(Integer mailId, String attachmentName) {
        MailAttachment mailAttachment = null;
        if (!StringUtil.isEmpty(attachmentName) && mailId != null) {
            mailAttachment = mailAttachmentDao.findByMailIdAndName(mailId, attachmentName);
        }
        return mailAttachment;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<MailAttachment> findAllByMailId(Integer mailId) {
        List<MailAttachment> attachments = null;
        if (mailId != null) {
            attachments = mailAttachmentDao.findAllByMailId(mailId);
        }
        return attachments;
    }

}
