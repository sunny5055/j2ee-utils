package com.google.code.jee.utils.mail.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.code.jee.utils.dal.dto.AbstractHibernateDto;
import com.google.code.jee.utils.mail.hibernate.dao.MailAttachmentDao;

/**
 * The Class MailAttachment.
 */
@Entity
@Table(name = "MAA_MAIL_ATTACHMENT")
@NamedQueries({
        @NamedQuery(name = MailAttachmentDao.COUNT_BY_NAME, query = "select count(*) from MailAttachment as m where m.name = :name"),
        @NamedQuery(name = MailAttachmentDao.FIND_BY_NAME, query = "from MailAttachment as m where m.name = :name"),
        @NamedQuery(name = MailAttachmentDao.COUNT_FOR_MAIL_ID, query = "select count(*) from Mail as m where m.id = :id"),
        @NamedQuery(name = MailAttachmentDao.FIND_ALL_BY_MAIL_ID, query = "select attachment from Mail as mai left join mai.attachments as attachment where mai.id = :mailId"),
        @NamedQuery(name = MailAttachmentDao.COUNT_FOR_MAIL_ID_AND_NAME, query = "select count(*) from Mail as m where m.id = :id and m.name = :name"),
        @NamedQuery(name = MailAttachmentDao.FIND_BY_MAIL_ID_AND_NAME, query = "select attachment from Mail as mai left join mai.attachments as attachment where mai.id = :mailId and attachment.name = :attachmentName") })
@SuppressWarnings("serial")
public class MailAttachment extends AbstractHibernateDto<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MAA_ID", nullable = false)
    private Integer id;

    @Column(name = "MAA_NAME", nullable = false, length = 100)
    private String name;

    @Lob
    @Column(name = "MAA_CONTENT", nullable = false)
    private byte[] content;

    /**
     * Instantiates a new mail attachment.
     */
    public MailAttachment() {
        super();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer getPrimaryKey() {
        return this.id;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setPrimaryKey(Integer primaryKey) {
        this.id = primaryKey;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the content.
     * 
     * @return the content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Sets the attachment.
     * 
     * @param content the new attachment
     */
    public void setAttachment(byte[] content) {
        this.content = content;
    }
}
