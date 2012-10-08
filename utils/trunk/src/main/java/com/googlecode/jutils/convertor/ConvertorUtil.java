package com.googlecode.jutils.convertor;

/**
 * Class ConvertorUtil.
 */
public final class ConvertorUtil {

    /**
     * Instantiates a new convertor util.
     */
    private ConvertorUtil() {
        super();
    }

    /**
     * Method toInteger.
     * 
     * @param string2convert the string2convert
     * @return Integer
     */
    public static Integer toInteger(String string2convert) {
        return toInteger(string2convert, null);
    }

    /**
     * Method toInteger.
     * 
     * @param string2convert the string2convert
     * @param defaultValue the defaultValue
     * @return Integer
     */
    public static Integer toInteger(String string2convert, Integer defaultValue) {
        try {
            return Integer.parseInt(string2convert);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Method toLong.
     * 
     * @param string2convert the string2convert
     * @return Long
     */
    public static Long toLong(String string2convert) {
        return toLong(string2convert, null);
    }

    /**
     * Method toLong.
     * 
     * @param string2convert the string2convert
     * @param defaultValue the defaultValue
     * @return Long
     */
    public static Long toLong(String string2convert, Long defaultValue) {
        try {
            return Long.parseLong(string2convert);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Method toDouble.
     * 
     * @param string2convert the string2convert
     * @return Double
     */
    public static Double toDouble(String string2convert) {
        return toDouble(string2convert, null);
    }

    /**
     * Method toDouble.
     * 
     * @param string2convert the string2convert
     * @param defaultValue the defaultValue
     * @return Double
     */
    public static Double toDouble(String string2convert, Double defaultValue) {
        try {
            return Double.parseDouble(string2convert);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Method toFloat.
     * 
     * @param string2convert the string2convert
     * @return Float
     */
    public static Float toFloat(String string2convert) {
        return toFloat(string2convert, null);
    }

    /**
     * Method toFloat.
     * 
     * @param string2convert the string2convert
     * @param defaultValue the defaultValue
     * @return Float
     */
    public static Float toFloat(String string2convert, Float defaultValue) {
        try {
            return Float.parseFloat(string2convert);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }
}
