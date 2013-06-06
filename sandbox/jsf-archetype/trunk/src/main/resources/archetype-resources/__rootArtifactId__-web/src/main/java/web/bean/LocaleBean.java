package ${package}.web.bean;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ${package}.web.util.FacesUtils;

@Controller
@Scope("session")
@SuppressWarnings("serial")
public class LocaleBean implements Serializable {
	private Locale locale;
	private String timeZone;
	private String datePattern;
	private String dateTimePattern;
	private String timePattern;

	public LocaleBean() {
		super();
	}

	@PostConstruct
	protected void init() {
		final Locale currentLocale = FacesUtils.getCurrentLocale();

		changeLocale(currentLocale);
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getDateTimePattern() {
		return dateTimePattern;
	}

	public void setDateTimePattern(String dateTimePattern) {
		this.dateTimePattern = dateTimePattern;
	}

	public String getTimePattern() {
		return timePattern;
	}

	public void setTimePattern(String timePattern) {
		this.timePattern = timePattern;
	}

	public void changeLocale(ValueChangeEvent event) {
		final Locale newLocale = (Locale) event.getNewValue();

		changeLocale(newLocale);
	}

	private void changeLocale(Locale newLocale) {
		if (newLocale != null) {
			this.locale = FacesUtils.updateLocale(newLocale);
			this.timeZone = FacesUtils.getLabel("time_zone");
			this.datePattern = FacesUtils.getLabel("date_pattern");
			this.dateTimePattern = FacesUtils.getLabel("datetime_pattern");
			this.timePattern = FacesUtils.getLabel("time_pattern");
		}
	}
}
