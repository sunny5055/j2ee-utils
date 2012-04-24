package com.google.code.jee.utils.mail.hibernate.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.code.jee.utils.dal.dto.AbstractHibernateDto;

/**
 * The Class Mail.
 */
@Entity
@Table(name = "MAI_MAIL")
@SuppressWarnings("serial")
public class Mail extends AbstractHibernateDto<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MAI_ID", nullable = false)
    private Integer id;

    @Column(name = "MAI_NAME", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "MAI_FROM", nullable = false, length = 100)
    private String from;

    @Column(name = "MAI_REPLY_TO", length = 100)
    private String replyTo;

    @Column(name = "MAI_SUBJECT", length = 250)
    private String subject;

    @Column(name = "MAI_TEXT", nullable = false, length = 2000)
    private String text;

    @Column(name = "MAI_HTML_MESSAGE", nullable = false)
    private Boolean htmlMessage;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinColumn(name = "MAA_MAIL_ID ", nullable = false)
    private List<MailAttachment> attachments;

    @Column(name = "MAI_TEMPLATE", nullable = false)
    private Boolean template;

    /**
     * Instantiates a new mail.
     */
    public Mail() {
        super();
        this.attachments = new ArrayList<MailAttachment>();
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
     * Getter : return the id
     * 
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter : affect the id
     * 
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter : return the name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter : affect the name
     * 
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter : return the from
     * 
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Setter : affect the from
     * 
     * @param from the from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Getter : return the replyTo
     * 
     * @return the replyTo
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * Setter : affect the replyTo
     * 
     * @param replyTo the replyTo
     */
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * Getter : return the subject
     * 
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Setter : affect the subject
     * 
     * @param subject the subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Getter : return the text
     * 
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Setter : affect the text
     * 
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Getter : return the htmlMessage
     * 
     * @return the htmlMessage
     */
    public Boolean getHtmlMessage() {
        return htmlMessage;
    }

    /**
     * Setter : affect the htmlMessage
     * 
     * @param htmlMessage the htmlMessage
     */
    public void setHtmlMessage(Boolean htmlMessage) {
        this.htmlMessage = htmlMessage;
    }

    /**
     * Getter : return the attachments
     * 
     * @return the attachments
     */
    public List<MailAttachment> getAttachments() {
        return attachments;
    }

    /**
     * Setter : affect the attachments
     * 
     * @param attachments the attachments
     */
    public void setAttachments(List<MailAttachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * Getter : return the template
     * 
     * @return the template
     */
    public Boolean getTemplate() {
        return template;
    }

    /**
     * Setter : affect the template
     * 
     * @param template the template
     */
    public void setTemplate(Boolean template) {
        this.template = template;
    }
}
