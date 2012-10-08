package com.googlecode.jutils.bundle.hibernate.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The Class LabelId.
 */
@Embeddable
@SuppressWarnings("serial")
public class LabelId implements Serializable {
    @Column(name = "LAB_KEY", length = 100, nullable = false)
    private String key;

    @Column(name = "LAB_LANGUAGE", length = 2, nullable = false)
    private String language;

    /**
     * The Constructor.
     */
    public LabelId() {
        super();
    }

    /**
     * Instantiates a new label id.
     * 
     * @param key the key
     * @param language the language
     */
    public LabelId(String key, String language) {
        this();
        this.key = key;
        this.language = language;
    }

    /**
     * Gets the key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets the language.
     * 
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language.
     * 
     * @param language the language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getKey()).append(this.getLanguage()).toHashCode();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LabelId other = (LabelId) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        if (language == null) {
            if (other.language != null) {
                return false;
            }
        } else if (!language.equals(other.language)) {
            return false;
        }
        return true;
    }
}
