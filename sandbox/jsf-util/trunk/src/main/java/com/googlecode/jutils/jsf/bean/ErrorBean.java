package com.googlecode.jutils.jsf.bean;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * The Class ErrorBean.
 */
@Controller
@Scope("session")
@SuppressWarnings("serial")
public class ErrorBean implements Serializable {
    private String title;
    private String message;

    /**
     * Instantiates a new error bean.
     */
    public ErrorBean() {
        super();
    }

    /**
     * Gets the title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     * 
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     * 
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
