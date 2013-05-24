#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericService;

public abstract class AbstractConverter<PK extends Serializable, DTO extends Dto<PK>, S extends GenericReadService<PK, DTO>>
        implements Converter {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractConverter.class);

    protected abstract Class<S> getServiceClass();

    protected abstract PK getPrimaryKey(String value);

    /**
     * {@inheritedDoc}
     */
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

    /**
     * {@inheritedDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        String toReturn = "";
        if (value != null) {
            if (value instanceof Dto) {
                final DTO dto = (DTO) value;

                toReturn = dto.getStringPrimaryKey();
            }
        }
        return toReturn;
    }
}
