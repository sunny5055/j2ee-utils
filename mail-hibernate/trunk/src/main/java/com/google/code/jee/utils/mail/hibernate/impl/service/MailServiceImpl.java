package com.google.code.jee.utils.mail.hibernate.impl.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.dal.service.AbstractGenericService;
import com.google.code.jee.utils.mail.hibernate.facade.dao.MailDao;
import com.google.code.jee.utils.mail.hibernate.facade.model.Mail;
import com.google.code.jee.utils.mail.hibernate.facade.service.MailService;

/**
 * The Class MailServiceImpl.
 */
@Service
public class MailServiceImpl extends AbstractGenericService<Integer, Mail, MailDao> implements MailService {

    /*
     * (non-Javadoc)
     * @see com.google.code.jee.utils.mail_hibernate.facade.service.MailService#
     * existWithName(java.lang.String)
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

    /*
     * (non-Javadoc)
     * @see com.google.code.jee.utils.mail_hibernate.facade.service.MailService#
     * findByName(java.lang.String)
     */
    @Override
    public Mail findByName(String name) {
        Mail mail = null;
        if (!StringUtils.isEmpty(name)) {
            mail = dao.findByName(name);
        }
        return mail;
    }

    /*
     * (non-Javadoc)
     * @see com.google.code.jee.utils.mail_hibernate.facade.service.MailService#
     * findAllByName()
     */
    @Override
    public List<Mail> findAllByName() {
        List<Mail> mails = dao.findAllByName();
        if (mails == null) {
            mails = new ArrayList<Mail>();
        }
        return mails;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.google.code.jee.utils.dal.service.AbstractGenericReadService#setDao
     * (com.google.code.jee.utils.dal.dao.GenericReadDao)
     */
    @Autowired
    @Override
    public void setDao(MailDao mailDao) {
        this.dao = mailDao;
    }

}
