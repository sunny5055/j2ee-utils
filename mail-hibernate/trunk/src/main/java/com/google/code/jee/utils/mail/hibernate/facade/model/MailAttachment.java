package com.google.code.jee.utils.mail.hibernate.facade.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.google.code.jee.utils.dal.dto.AbstractHibernateDto;

/**
 * The Class MailAttachment.
 */
@Entity
@Table(name = "MAA_MAIL_ATTACHMENT")
public class MailAttachment extends AbstractHibernateDto<Integer> implements Serializable {
    
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MAA_ID", nullable = false, unique = true)
    private Integer id;

    /** The name. */
    @Column(name = "MAA_NAME", nullable = false)
    private String name;

    /** The content. */
    @Lob
    @Column(name = "MAA_CONTENT", nullable = false)
    private byte[] content;

    /* (non-Javadoc)
     * @see com.google.code.jee.utils.dal.dto.Dto#getPrimaryKey()
     */
    @Override
    public Integer getPrimaryKey() {
        return this.id;
    }

    /**
     * Instantiates a new mail attachment.
     */
    public MailAttachment() {
        super();
    }
    
    /* (non-Javadoc)
     * @see com.google.code.jee.utils.dal.dto.Dto#setPrimaryKey(java.io.Serializable)
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
