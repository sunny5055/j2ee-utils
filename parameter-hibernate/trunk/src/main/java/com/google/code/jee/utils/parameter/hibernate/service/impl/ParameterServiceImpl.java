package com.google.code.jee.utils.parameter.hibernate.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.BooleanUtil;
import com.google.code.jee.utils.DateUtil;
import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.dal.Result;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.parameter.hibernate.dao.ParameterDao;
import com.google.code.jee.utils.parameter.hibernate.enumeration.DateFormatEnum;
import com.google.code.jee.utils.parameter.hibernate.model.AbstractParameter;
import com.google.code.jee.utils.parameter.hibernate.model.BooleanParameter;
import com.google.code.jee.utils.parameter.hibernate.model.DateParameter;
import com.google.code.jee.utils.parameter.hibernate.model.FloatParameter;
import com.google.code.jee.utils.parameter.hibernate.model.IntegerParameter;
import com.google.code.jee.utils.parameter.hibernate.model.StringParameter;
import com.google.code.jee.utils.parameter.hibernate.service.ParameterService;

@Service
public class ParameterServiceImpl implements ParameterService {
    
    @Autowired
    private ParameterDao dao;

    private String dateFormat;

    /**
     * Instantiates a new parameter service impl.
     */
    public ParameterServiceImpl() {
        super();
        this.dateFormat = DateFormatEnum.DATE_TIME.getValue();
    }
    
    /**
     * {@inheritedDoc}
     */
    @Autowired
    public void setDao(ParameterDao parameterDao) {
        this.dao = parameterDao;
    }

    /**
     * Gets the date format.
     * 
     * @return the date format
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Sets the date format.
     * 
     * @param dateFormat the new date format
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
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
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public <V> void setValue(String name, V value) {
        this.setValue(name, null, value);
    }
    
   /** 
    * {@inheritedDoc}
    * 
    * @throws IllegalAccessException
    * @throws InstantiationException
    */
   @Override
   public <V> void setValue(String name, String description, V value) {
	  this.setValue(name, description, value, null);   
   }

   /** 
    * {@inheritedDoc}
    * 
    * @throws IllegalAccessException
    * @throws InstantiationException
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
		            parameter.setDescription(description);
		        }
		
		    } else {
		        parameter = dao.findByName(name);
		    }
		
		    if (parameter != null) {
		        if (parameter instanceof BooleanParameter && value instanceof Boolean) {
		            ((BooleanParameter) parameter).setValue((Boolean) value);
		        } else if (parameter instanceof FloatParameter && value instanceof Float) {
		            ((FloatParameter) parameter).setValue((Float) value);
		        } else if (parameter instanceof IntegerParameter && value instanceof Integer) {
		            ((IntegerParameter) parameter).setValue((Integer) value);
		        } else if (parameter instanceof DateParameter && value instanceof Date) {
		            ((DateParameter) parameter).setValue((Date) value);
		            if (format != null)
		            	((DateParameter) parameter).setDateFormat(format);
		            else {
		            	((DateParameter) parameter).setDateFormat(DateFormatEnum.DATE_TIME.getValue());
		            }
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
            if (existWithName(name)) {
                final AbstractParameter<?> parameter = dao.findByName(name);
                if (parameter != null) {
                    deleted = dao.delete(parameter);
                }
            }
        }
        return deleted;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer removeAllValues() {
        Integer deleted = 0;
        final List<AbstractParameter<?>> parameters = this.dao.findAll();
        if (!CollectionUtils.isEmpty(parameters)) {
            for (final AbstractParameter<?> parameter : parameters) {
                deleted += this.dao.delete(parameter);
            }
        }
        return deleted;
    }

    /**
     * {@inheritedDoc}
     * 
     * @throws IOException
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
                    	String format = ((DateParameter) parameter).getDateFormat();
                    	if (StringUtils.isBlank(format)) {
                    		format = dateFormat;
                    	}
                        value = DateUtil.format((Date) parameter.getValue(), format);
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
     * 
     * @throws IOException
     */
    @Override
    public void importProperties(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            if (StringUtil.isBlank(dateFormat)) {
                dateFormat =  DateFormatEnum.DATE_TIME.getValue();
            }

            // Load properties
            final Properties properties = new Properties();
            properties.load(inputStream);

            for (final Object key : properties.keySet()) {
                final String keyString = (String) key;
                final String value = properties.getProperty(keyString);
                Object convertedValue = null;

                // Date
                try {
                	DateFormat df = DateFormat.getInstance();
                    convertedValue = df.parse(value);
                } catch (final ParseException e) {
                    convertedValue = null;
                }

                // Integer
                if (convertedValue == null) {
                    try {
                        convertedValue = Integer.parseInt(value);
                    } catch (final Exception e) {
                        convertedValue = null;
                    }
                }

                // Float
                if (convertedValue == null) {
                    try {
                        convertedValue = Float.parseFloat(value);
                    } catch (final Exception e) {
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
                	String format = DateFormatEnum.DATE_TIME.getValue();
                	if (value.matches("\\d{2}/\\d{2}/\\d{4}"))
                		format = DateFormatEnum.DATE_ONLY.getValue();
                	else if (value.matches("\\d{2}:\\d{2}:\\d{2}"))
                		format = DateFormatEnum.TIME_ONLY.getValue();
                	setValue(keyString, null, convertedValue, format);
                } else
                	setValue(keyString, convertedValue);
            }
        }
    }
 
    /**
     * {@inheritedDoc}
     */
    @Override
    public Result<AbstractParameter<?>> update(AbstractParameter<?> parameter) {
        final Result<AbstractParameter<?>> result = new Result<AbstractParameter<?>>();
        if (parameter != null) {
            final Integer updated = this.dao.save(parameter);
            if (updated != 0) {
                final AbstractParameter<?> entity = this.findByName(parameter.getName());
                if (entity != null) {
                    result.setValid(true);
                    result.setValue(entity);
                } else {
                    result.setValid(false);
                    result.addErrorMessage("error_updated_entity_not_found");
                }
            } else {
                result.setValid(false);
                result.addErrorMessage("error_update_failed");
            }
        } else {
            result.setValid(false);
            result.addErrorMessage("error_wrong_parameter");
        }
        return result;
    }

}
