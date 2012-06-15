package com.google.code.jee.utils.jsf.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.log4j.Logger;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.dto.Dto;
import com.google.code.jee.utils.dal.service.GenericReadService;

public abstract class AbstractConverter<PK extends Serializable, E extends Dto<PK>, S extends GenericReadService<PK, E>>
        implements Converter {
    protected static final Logger LOGGER = Logger.getLogger(AbstractConverter.class);

    protected abstract Class<S> getServiceClass();

    protected abstract PK getPrimaryKey(String value);

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        Object toReturn = null;
        if (!StringUtil.isBlank(value)) {
            final ApplicationContext context = FacesContextUtils.getWebApplicationContext(facesContext);
            final S service = context.getBean(getServiceClass());

            final PK pk = getPrimaryKey(value);
            if (pk != null) {
                toReturn = service.get(pk);
            }
        }
        return toReturn;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        String toReturn = "";
        if (value != null) {
            if (value instanceof Dto) {
                PK primaryKey = null;
                if (value instanceof HibernateProxy) {
                    primaryKey = (PK) ((HibernateProxy) value).getHibernateLazyInitializer().getIdentifier();
                } else {
                    final E dto = (E) value;
                    primaryKey = dto.getPrimaryKey();
                }

                if (primaryKey != null) {
                    toReturn = primaryKey.toString();
                }
            }
        }
        return toReturn;
    }
}
