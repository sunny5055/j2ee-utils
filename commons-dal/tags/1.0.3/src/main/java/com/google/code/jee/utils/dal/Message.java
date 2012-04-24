package com.google.code.jee.utils.dal;

import com.google.code.jee.utils.collection.ArrayUtil;

/**
 * The Class Message.
 */
public class Message {
    private String bundleKey;
    private Object[] args;

    /**
     * Instantiates a new message.
     */
    public Message() {
        super();
    }

    /**
     * Instantiates a new message.
     * 
     * @param bundleKey the bundle key
     */
    public Message(String bundleKey) {
        super();
        this.bundleKey = bundleKey;
    }

    /**
     * Instantiates a new message.
     * 
     * @param bundleKey the bundle key
     * @param args the args
     */
    public Message(String bundleKey, Object[] args) {
        super();
        this.bundleKey = bundleKey;
        this.args = args;
    }

    /**
     * Getter : return the bundleKey.
     * 
     * @return the bundleKey
     */
    public String getBundleKey() {
        return bundleKey;
    }

    /**
     * Setter : affect the bundleKey.
     * 
     * @param bundleKey the bundleKey
     */
    public void setBundleKey(String bundleKey) {
        this.bundleKey = bundleKey;
    }

    /**
     * Getter : return the args.
     * 
     * @return the args
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * Setter : affect the args.
     * 
     * @param args the args
     */
    public void setArgs(Object[] args) {
        this.args = args;
    }

    /**
     * Checks for args.
     * 
     * @return true, if successful
     */
    public boolean hasArgs() {
        return !ArrayUtil.isEmpty(args);
    }
}
