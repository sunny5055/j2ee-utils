package com.google.code.jee.utils.mail_hibernate.impl.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.dal.service.AbstractGenericService;
import com.google.code.jee.utils.mail_hibernate.facade.dao.MailAttachmentDao;
import com.google.code.jee.utils.mail_hibernate.facade.model.MailAttachment;
import com.google.code.jee.utils.mail_hibernate.facade.service.MailAttachmentService;

/**
 * The Class MailAttachmentServiceImpl.
 */
@Service
public class MailAttachmentServiceImpl extends AbstractGenericService<Integer, MailAttachment, MailAttachmentDao>
        implements MailAttachmentService {

    /* (non-Javadoc)
     * @see com.google.code.jee.utils.mail_hibernate.facade.service.MailAttachmentService#existWithName(java.lang.String)
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

    /* (non-Javadoc)
     * @see com.google.code.jee.utils.mail_hibernate.facade.service.MailAttachmentService#findByName(java.lang.String)
     */
    @Override
    public MailAttachment findByName(String name) {
        MailAttachment mailAttachment = null;
        if (!StringUtils.isEmpty(name)) {
            mailAttachment = dao.findByName(name);
        }
        return mailAttachment;
    }

    /* (non-Javadoc)
     * @see com.google.code.jee.utils.mail_hibernate.facade.service.MailAttachmentService#findAllByName()
     */
    @Override
    public List<MailAttachment> findAllByName() {
        List<MailAttachment> attachments = dao.findAllByName();
        if (attachments == null) {
            attachments = new ArrayList<MailAttachment>();
        }
        return attachments;
    }

    /* (non-Javadoc)
     * @see com.google.code.jee.utils.dal.service.AbstractGenericReadService#setDao(com.google.code.jee.utils.dal.dao.GenericReadDao)
     */
    @Autowired
    @Override
    public void setDao(MailAttachmentDao mailAttachmentDao) {
        this.dao = mailAttachmentDao;
    }

}
