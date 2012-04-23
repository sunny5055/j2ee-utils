package com.google.code.jee.utils.mail.hibernate.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /** The mail attachment dao. */
    @Autowired
    private MailAttachmentDao mailAttachmentDao; 

    /**
    * {@inheritedDoc}
    */
    
    @Override
    public boolean existWithName(String name) {
        boolean exist = false;
        if (!StringUtils.isEmpty(name)) {
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
        if (!StringUtils.isEmpty(name)) {
            mail = dao.findByName(name);
        }
        return mail;
    }

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
    public MailAttachment findMailAttachment(Integer mailPrimaryKey, String attachmentName) {
        MailAttachment mailAttachment = null;
        if (!StringUtils.isEmpty(attachmentName) && mailPrimaryKey != null) {
            mailAttachment = mailAttachmentDao.findMailAttachment(mailPrimaryKey, attachmentName);
        }
        return mailAttachment;
    }

    /**
    * {@inheritedDoc}
    */
    
    @Override
    public List<MailAttachment> findAllAttachments(Integer mailPrimaryKey) {
        List<MailAttachment> attachments = null;
        if(mailPrimaryKey != null) {
            mailAttachmentDao.findAllMailAttachments(mailPrimaryKey);
        }
        if (attachments == null) {
            attachments = new ArrayList<MailAttachment>();
        }
        return attachments;
    }

}
