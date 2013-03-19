package com.googlecode.jutils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * The Class DateUtil.
 */
public final class DateUtil extends DateUtils {

    /**
     * Instantiates a new date util.
     */
    private DateUtil() {
    }

    /**
     * Returns the actual date in SQL format.
     * 
     * @return the java.sql. date
     */
    public static java.sql.Date nowSqlDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }

    /**
     * Returns the actual date in Java format.
     * 
     * @return the java.util. date
     */
    public static java.util.Date nowDate() {
        return new java.util.Date();
    }

    /**
     * Format.
     * 
     * @param date the date
     * @param format the format
     * @return the string
     */
    public static String format(java.sql.Date date, String format) {
        String stringDate = null;
        if (date != null && !StringUtil.isBlank(format)) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            stringDate = simpleDateFormat.format(date);
        }
        return stringDate;
    }

    /**
     * Format.
     * 
     * @param date the date
     * @param format the format
     * @return the string
     */
    public static String format(java.util.Date date, String format) {
        String stringDate = null;
        if (date != null && !StringUtil.isBlank(format)) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            stringDate = simpleDateFormat.format(date);
        }
        return stringDate;
    }

    /**
     * Compare date.
     * 
     * @param date1 the date1
     * @param date2 the date2
     * @return the integer
     */
    public static Integer compareDate(Date date1, Date date2) {
        return compareDate(date1, date2, false);
    }

    /**
     * Compare date.
     * 
     * @param date1 the date1
     * @param date2 the date2
     * @param truncateTime the truncate time
     * @return the integer
     */
    public static Integer compareDate(Date date1, Date date2, boolean truncateTime) {
        Integer compare = null;
        if (date1 != null && date2 != null) {
            Date cdate1 = date1;
            Date cdate2 = date2;
            if (truncateTime) {
                cdate1 = setTimeToMidnight(date1);
                cdate2 = setTimeToMidnight(date2);
            }
            if (DateUtil.isSameDay(cdate1, cdate2)) {
                compare = 0;
            } else if (cdate1.before(cdate2)) {
                compare = -1;
            } else if (cdate1.after(cdate2)) {
                compare = 1;
            }
        }
        return compare;
    }

    /**
     * Sets the time to midnight.
     * 
     * @param date the date
     * @return the date
     */
    public static Date setTimeToMidnight(Date date) {
        Date newDate = null;
        if (date != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            newDate = calendar.getTime();
        }

        return newDate;
    }
}
