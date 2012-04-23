package com.google.code.jee.utils.mail_hibernate.facade.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

import com.google.code.jee.utils.dal.dto.AbstractHibernateDto;

/**
 * The Class Mail.
 */
@Entity
@Table(name = "MAI_MAIL")
public class Mail extends AbstractHibernateDto<Integer> implements Serializable {
    
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MAI_ID", nullable = false, unique = true)
    private Integer id;

    /** The from. */
    @Column(name = "MAI_FROM", nullable = false)
    private String from;

    /** The reply to. */
    @Column(name = "MAI_REPLY_TO")
    private String replyTo;

    /** The subject. */
    @Column(name = "MAI_SUBJECT")
    private String subject;

    /** The text. */
    @Column(name = "MAI_TEXT", nullable = false)
    private String text;

    /** The html message. */
    @Column(name = "MAI_HTML_MESSAGE")
    private Boolean htmlMessage;

    /** The attachments. */
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinColumn(name = "MAA_MAIL_ID ", nullable = false)
    private List<MailAttachment> attachments;

    /** The template. */
    @Column(name = "MAI_TEMPLATE")
    private Boolean template;
    
    /** The template name. */
    @Column(name = "MAI_NAME")
    private Boolean name;

    /**
     * Instantiates a new mail.
     */
    public Mail() {
        super();
        attachments = new ArrayList<MailAttachment>();
    }

    /* (non-Javadoc)
     * @see com.google.code.jee.utils.dal.dto.Dto#getPrimaryKey()
     */
    @Override
    public Integer getPrimaryKey() {
        return this.id;
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
     * Gets the from.
     *
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the from.
     *
     * @param from the new from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gets the reply to.
     *
     * @return the reply to
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * Sets the reply to.
     *
     * @param replyTo the new reply to
     */
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject.
     *
     * @param subject the new subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text.
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Checks if is html message.
     *
     * @return the boolean
     */
    public Boolean isHtmlMessage() {
        return htmlMessage;
    }

    /**
     * Sets the html message.
     *
     * @param htmlMessage the new html message
     */
    public void setHtmlMessage(Boolean htmlMessage) {
        this.htmlMessage = htmlMessage;
    }

    /**
     * Gets the attachments.
     *
     * @return the attachments
     */
    public List<MailAttachment> getAttachments() {
        return attachments;
    }

    /**
     * Sets the attachments.
     *
     * @param attachments the new attachments
     */
    public void setAttachments(List<MailAttachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * Checks if is template.
     *
     * @return the boolean
     */
    public Boolean isTemplate() {
        return template;
    }

    /**
     * Sets the template.
     *
     * @param template the new template
     */
    public void setTemplate(Boolean template) {
        this.template = template;
    }

    public Boolean getName() {
        return name;
    }

    public void setName(Boolean name) {
        this.name = name;
    }
}
