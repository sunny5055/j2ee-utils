package com.googlecode.jutils.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.jsf.util.FacesUtils;

@Controller
@Scope("session")
@SuppressWarnings("serial")
public class LocaleBean implements Serializable {
    private Locale locale;
    private String datePattern;
    private String dateTimePattern;
    private String timePattern;
    private Locale defaultLocale;

    /**
     * Instantiates a new locale bean.
     */
    public LocaleBean() {
        super();
    }

    /**
     * Initialiaze th bean
     * <p/>
     * .
     */
    @PostConstruct
    protected void init() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Locale currentLocale = facesContext.getViewRoot().getLocale();
        if (defaultLocale == null) {
            defaultLocale = currentLocale;
        }
        this.locale = FacesUtils.updateLocale(facesContext, currentLocale);
        this.datePattern = FacesUtils.getLabel(facesContext, "date_pattern");
        this.dateTimePattern = FacesUtils.getLabel(facesContext, "datetime_pattern");
        this.timePattern = FacesUtils.getLabel(facesContext, "time_pattern");
    }

    /**
     * Getter : return the locale.
     * 
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Setter : affect the locale.
     * 
     * @param locale the locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Getter : return the datePattern
     * 
     * @return the datePattern
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * Getter : return the dateTimePattern
     * 
     * @return the dateTimePattern
     */
    public String getDateTimePattern() {
        return dateTimePattern;
    }

    /**
     * Getter : return the timePattern
     * 
     * @return the timePattern
     */
    public String getTimePattern() {
        return timePattern;
    }

    /**
     * This method is called after an user login to set the locale
     * 
     * @param userLang the user default language
     */
    public void afterLogin(String userLang) {
        if (!StringUtil.isBlank(userLang)) {
            final Locale userLocale = new Locale(userLang);
            this.locale = FacesUtils.updateLocale(FacesContext.getCurrentInstance(), userLocale);

            final FacesContext facesContext = FacesContext.getCurrentInstance();
            this.datePattern = FacesUtils.getLabel(facesContext, "date_pattern");
            this.dateTimePattern = FacesUtils.getLabel(facesContext, "datetime_pattern");
            this.timePattern = FacesUtils.getLabel(facesContext, "time_pattern");
        }
    }

    public void afterLogout() {
        this.locale = FacesUtils.updateLocale(FacesContext.getCurrentInstance(), defaultLocale);

        final FacesContext facesContext = FacesContext.getCurrentInstance();
        this.datePattern = FacesUtils.getLabel(facesContext, "date_pattern");
        this.dateTimePattern = FacesUtils.getLabel(facesContext, "datetime_pattern");
        this.timePattern = FacesUtils.getLabel(facesContext, "time_pattern");
    }

    /**
     * Gets the display language.
     * 
     * @return the display language
     */
    public String getDisplayLanguage() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        return FacesUtils.getLabel(facesContext, "lang_" + this.locale.getLanguage());
    }

    /**
     * Gets the supported locales.
     * 
     * @return the supported locales
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSupportedLocales() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final List<SelectItem> supportedLocales = new ArrayList<SelectItem>();
        final List<Locale> locales = IteratorUtils.toList(facesContext.getApplication().getSupportedLocales());
        if (!CollectionUtil.isEmpty(locales)) {
            for (final Locale locale : locales) {
                supportedLocales.add(new SelectItem(locale, FacesUtils.getLabel(facesContext,
                        "lang_" + locale.getLanguage())));
            }
        }
        return supportedLocales;
    }

    /**
     * Change Locale value according to the selected flag or language.
     * <p/>
     * 
     * @param event the ValueChangeEvent
     */
    public void changeLocale(ValueChangeEvent event) {
        final Locale newLocale = (Locale) event.getNewValue();
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        this.locale = FacesUtils.updateLocale(facesContext, newLocale);

        this.datePattern = FacesUtils.getLabel(facesContext, "date_pattern");
        this.dateTimePattern = FacesUtils.getLabel(facesContext, "datetime_pattern");
        this.timePattern = FacesUtils.getLabel(facesContext, "time_pattern");
    }
}
