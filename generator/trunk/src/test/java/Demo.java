import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.jee.utils.generator.hibernate.config.HibernateConfig;
import com.googlecode.jee.utils.generator.hibernate.model.Entity;
import com.googlecode.jee.utils.generator.hibernate.model.Property;
import com.googlecode.jee.utils.generator.hibernate.model.SimpleValue;
import com.googlecode.jee.utils.generator.hibernate.model.Value;
import com.googlecode.jutils.oxm.exception.OxmServiceException;
import com.googlecode.jutils.oxm.service.OxmService;
import com.googlecode.jutils.templater.exception.TemplaterServiceException;

public class Demo {
	private static OxmService oxmService;

	public static void main(String[] args) throws TemplaterServiceException, IOException, ConfigurationException, OxmServiceException {
		final ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring/application-context.xml" });
		oxmService = context.getBean(OxmService.class);

		marshall();

		// unmarshall();
	}

	private static void marshall() throws OxmServiceException {
		final HibernateConfig hibernateConfig = new HibernateConfig();
		hibernateConfig.setProjectName("user-management");
		hibernateConfig.setVersion("1.0.0");
		hibernateConfig.setBasePackageName("com.google.code.jee.utils.user.management");

		final Entity user = getUserEntity();
		hibernateConfig.addEntity(user);

		oxmService.marshall(hibernateConfig, new File("user.xml"));
	}

	private static void unmarshall() throws OxmServiceException {
		final HibernateConfig hibernateConfig = (HibernateConfig) oxmService.unmarshall(new File("user.xml"));
		if (hibernateConfig != null) {
			System.out.println();
		}
	}

	private static Entity getUserEntity() {
		final Entity user = new Entity();
		user.setPackageName("com.google.code.jee.utils.user.management.model");
		user.setClassName("User");
		user.setTableName("USR_USER");

		user.addProperty(getProperty("id", "Integer", false, true));
		user.addProperty(getProperty("firstName", "String", true, false));
		user.addProperty(getProperty("lastName", "String", true, false));
		user.addProperty(getProperty("login", "String", false, true));
		user.addProperty(getProperty("password", "String", false, false));
		user.addProperty(getProperty("mail", "String", false, true));
		user.addProperty(getProperty("active", "boolean", false, false));
		// user.addProperty(getProperty("roles"));

		return user;
	}

	private static Property getProperty(String name, String typeName, boolean nullable, boolean unique) {
		final Property property = new Property();
		property.setName(name);
		property.setValue(getValue(typeName, nullable, unique));
		return property;
	}

	private static Value getValue(String typeName, boolean nullable, boolean unique) {
		final SimpleValue value = new SimpleValue();
		value.setTypeName(typeName);
		value.setNullable(nullable);
		value.setUnique(unique);
		return value;
	}
}
