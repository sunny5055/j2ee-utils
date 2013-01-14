package com.googlecode.jutils.dal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.googlecode.jutils.StringUtil;

/**
 * The Class Result.
 * 
 * @param <T> the generic type
 */
public class Result<T> {
    private boolean valid;
    private T value;
    private List<Message> infoMessages;
    private List<Message> warnMessages;
    private List<Message> errorMessages;

    public Result() {
        super();
        this.infoMessages = new ArrayList<Message>();
        this.warnMessages = new ArrayList<Message>();
        this.errorMessages = new ArrayList<Message>();
    }

    /**
     * Getter : return the valid.
     * 
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Setter : affect the valid.
     * 
     * @param valid the valid
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Getter : return the value.
     * 
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * Setter : affect the value.
     * 
     * @param value the value
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Getter : return the infoMessages
     * 
     * @return the infoMessages
     */
    public List<Message> getInfoMessages() {
        return infoMessages;
    }

    /**
     * Setter : affect the infoMessages
     * 
     * @param infoMessages the infoMessages
     */
    public void setInfoMessages(List<Message> infoMessages) {
        this.infoMessages = infoMessages;
    }

    /**
     * Adds the info message.
     * 
     * @param bundleKey the bundle key
     * @param args the args
     */
    public void addInfoMessage(String bundleKey, Object... args) {
        if (!StringUtil.isBlank(bundleKey)) {
            final Message message = new Message(bundleKey, args);
            infoMessages.add(message);
        }
    }

    /**
     * Checks for info message.
     * 
     * @return true, if successful
     */
    public boolean hasInfoMessage() {
        return !CollectionUtils.isEmpty(infoMessages);
    }

    /**
     * Getter : return the warnMessages
     * 
     * @return the warnMessages
     */
    public List<Message> getWarnMessages() {
        return warnMessages;
    }

    /**
     * Setter : affect the warnMessages
     * 
     * @param warnMessages the warnMessages
     */
    public void setWarnMessages(List<Message> warnMessages) {
        this.warnMessages = warnMessages;
    }

    /**
     * Adds the warn message.
     * 
     * @param bundleKey the bundle key
     * @param args the args
     */
    public void addWarnMessage(String bundleKey, Object... args) {
        if (!StringUtil.isBlank(bundleKey)) {
            final Message message = new Message(bundleKey, args);
            warnMessages.add(message);
        }
    }

    /**
     * Checks for warn message.
     * 
     * @return true, if successful
     */
    public boolean hasWarnMessage() {
        return !CollectionUtils.isEmpty(warnMessages);
    }

    /**
     * Getter : return the errorMessages
     * 
     * @return the errorMessages
     */
    public List<Message> getErrorMessages() {
        return errorMessages;
    }

    /**
     * Setter : affect the errorMessages
     * 
     * @param errorMessages the errorMessages
     */
    public void setErrorMessages(List<Message> errorMessages) {
        this.errorMessages = errorMessages;
    }

    /**
     * Adds the error message.
     * 
     * @param bundleKey the bundle key
     * @param args the args
     */
    public void addErrorMessage(String bundleKey, Object... args) {
        if (!StringUtil.isBlank(bundleKey)) {
            final Message message = new Message(bundleKey, args);
            errorMessages.add(message);
        }
    }

    /**
     * Checks for error message.
     * 
     * @return true, if successful
     */
    public boolean hasErrorMessage() {
        return !CollectionUtils.isEmpty(errorMessages);
    }

    /**
     * Adds the messages.
     * 
     * @param otherResult the other result
     */
    public void addMessages(Result<?> otherResult) {
        if (otherResult != null) {
            this.infoMessages.addAll(otherResult.getInfoMessages());
            this.warnMessages.addAll(otherResult.getWarnMessages());
            this.errorMessages.addAll(otherResult.getErrorMessages());
        }
    }

}
