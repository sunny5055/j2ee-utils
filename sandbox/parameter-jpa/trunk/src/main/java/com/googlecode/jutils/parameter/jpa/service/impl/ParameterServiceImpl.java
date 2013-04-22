package com.googlecode.jutils.parameter.jpa.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.BooleanUtil;
import com.googlecode.jutils.DateUtil;
import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.parameter.jpa.dao.ParameterDao;
import com.googlecode.jutils.parameter.jpa.enumeration.DateFormatEnum;
import com.googlecode.jutils.parameter.jpa.model.AbstractParameter;
import com.googlecode.jutils.parameter.jpa.model.BooleanParameter;
import com.googlecode.jutils.parameter.jpa.model.DateParameter;
import com.googlecode.jutils.parameter.jpa.model.FloatParameter;
import com.googlecode.jutils.parameter.jpa.model.IntegerParameter;
import com.googlecode.jutils.parameter.jpa.model.StringParameter;
import com.googlecode.jutils.parameter.jpa.service.ParameterService;

@Service
public class ParameterServiceImpl implements ParameterService {
	@Autowired
	private ParameterDao dao;

	/**
	 * Instantiates a new parameter service impl.
	 */
	public ParameterServiceImpl() {
		super();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Autowired
	public void setDao(ParameterDao parameterDao) {
		this.dao = parameterDao;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer count() {
		return dao.count();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<AbstractParameter<?>> findAll() {
		return dao.findAll();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer count(SearchCriteria searchCriteria) {
		return dao.count(searchCriteria);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<AbstractParameter<?>> findAll(SearchCriteria searchCriteria) {
		return dao.findAll(searchCriteria);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public boolean existWithName(String name) {
		boolean exist = false;
		if (!StringUtil.isEmpty(name)) {
			final Integer count = dao.countByName(name);
			exist = count != 0;
		}
		return exist;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public AbstractParameter<?> findByName(String name) {
		AbstractParameter<?> parameter = null;
		if (!StringUtil.isEmpty(name)) {
			parameter = dao.findByName(name);
		}
		return parameter;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V> V getValue(String name) {
		V value = null;
		if (!StringUtil.isBlank(name)) {
			final AbstractParameter<?> parameter = dao.findByName(name);
			if (parameter != null) {
				value = (V) parameter.getValue();
			}
		}
		return value;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public <V> void setValue(String name, V value) {
		this.setValue(name, null, value);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public <V> void setValue(String name, String description, V value) {
		this.setValue(name, description, value, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public <V> void setValue(String name, String description, V value, String format) {
		if (!StringUtil.isBlank(name) && value != null) {
			AbstractParameter<?> parameter = null;
			if (!existWithName(name)) {
				if (value instanceof Boolean) {
					parameter = new BooleanParameter();
				} else if (value instanceof Float) {
					parameter = new FloatParameter();
				} else if (value instanceof Integer) {
					parameter = new IntegerParameter();
				} else if (value instanceof Date) {
					parameter = new DateParameter();
				} else {
					parameter = new StringParameter();
				}

				if (parameter != null) {
					parameter.setName(name);
				}
			} else {
				parameter = dao.findByName(name);
			}

			if (parameter != null) {
				if (!StringUtil.isBlank(description)) {
					parameter.setDescription(description);
				}

				if (parameter instanceof BooleanParameter && value instanceof Boolean) {
					((BooleanParameter) parameter).setValue((Boolean) value);
				} else if (parameter instanceof FloatParameter && value instanceof Float) {
					((FloatParameter) parameter).setValue((Float) value);
				} else if (parameter instanceof IntegerParameter && value instanceof Integer) {
					((IntegerParameter) parameter).setValue((Integer) value);
				} else if (parameter instanceof DateParameter && value instanceof Date) {
					String dateFormat = format;
					if (StringUtil.isBlank(dateFormat)) {
						dateFormat = DateFormatEnum.DATE_ONLY.getValue();
					}

					Date date = (Date) value;
					if (StringUtil.equalsIgnoreCase(dateFormat, DateFormatEnum.TIME_ONLY.getValue())) {
						final Calendar calendar = Calendar.getInstance();
						calendar.clear();
						calendar.setTime(date);
						calendar.set(Calendar.DAY_OF_MONTH, 1);
						calendar.set(Calendar.MONTH, 0);
						calendar.set(Calendar.YEAR, 1970);

						date = calendar.getTime();
					} else if (StringUtil.equalsIgnoreCase(dateFormat, DateFormatEnum.DATE_ONLY.getValue())) {
						final Calendar calendar = Calendar.getInstance();
						calendar.clear();
						calendar.setTime(date);
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);

						date = calendar.getTime();
					}

					((DateParameter) parameter).setValue(date);
					((DateParameter) parameter).setDateFormat(dateFormat);
				} else if (parameter instanceof StringParameter) {
					((StringParameter) parameter).setValue(value.toString());
				}

				dao.save(parameter);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer removeValue(String name) {
		Integer deleted = 0;
		if (!StringUtil.isBlank(name)) {
			deleted = dao.deleteByName(name);
		}
		return deleted;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void exportProperties(OutputStream outputStream) throws IOException {
		if (outputStream != null) {
			final List<AbstractParameter<?>> abstractParameters = dao.findAll();
			if (!CollectionUtil.isEmpty(abstractParameters)) {
				final Properties properties = new Properties();
				for (final AbstractParameter<?> parameter : abstractParameters) {
					String value = null;
					if (parameter.getValue() instanceof Date) {
						value = ((DateParameter) parameter).getValueAsString();
					} else {
						value = parameter.getValue().toString();
					}

					properties.put(parameter.getName(), value);
				}
				properties.store(outputStream, null);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void importProperties(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			// Load properties
			final Properties properties = new Properties();
			properties.load(inputStream);

			for (final Object key : properties.keySet()) {
				final String keyString = (String) key;
				final String value = properties.getProperty(keyString);
				Object convertedValue = null;
				String format = null;

				// Date only
				try {
					convertedValue = DateUtil.parseDate(value, DateFormatEnum.DATE_ONLY.getValue());
					format = DateFormatEnum.DATE_ONLY.getValue();
				} catch (final ParseException e) {
					convertedValue = null;
				}

				// Time only
				if (convertedValue == null) {
					try {
						convertedValue = DateUtil.parseDate(value, DateFormatEnum.TIME_ONLY.getValue());
						format = DateFormatEnum.TIME_ONLY.getValue();
					} catch (final ParseException e) {
						convertedValue = null;
					}
				}

				// Date time
				if (convertedValue == null) {
					try {
						convertedValue = DateUtil.parseDate(value, DateFormatEnum.DATE_TIME.getValue());
						format = DateFormatEnum.DATE_TIME.getValue();
					} catch (final ParseException e) {
						convertedValue = null;
					}
				}

				// Integer
				if (convertedValue == null) {
					try {
						convertedValue = Integer.parseInt(value);
					} catch (final NumberFormatException e) {
						convertedValue = null;
					}

				}

				// Float
				if (convertedValue == null) {
					try {
						convertedValue = Float.parseFloat(value);
					} catch (final NumberFormatException e) {
						convertedValue = null;
					}
				}

				// Boolean
				if (convertedValue == null) {
					convertedValue = BooleanUtil.toBooleanObject(value);
				}

				// String
				if (convertedValue == null) {
					convertedValue = value;
				}

				if (convertedValue instanceof Date) {
					setValue(keyString, null, convertedValue, format);
				} else {
					setValue(keyString, convertedValue);
				}
			}
		}
	}

}
