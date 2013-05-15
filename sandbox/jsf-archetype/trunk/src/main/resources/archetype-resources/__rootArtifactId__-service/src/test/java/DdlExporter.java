import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;

import org.apache.commons.io.FileUtils;

import org.hibernate.cfg.Configuration;

import org.hibernate.dialect.Dialect;

import org.hibernate.engine.jdbc.internal.DDLFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.ManagedType;


public class DdlExporter {
    private static ApplicationContext context;

    public static void main(String[] args) throws IOException {
        context = new ClassPathXmlApplicationContext(
                "spring/business-context.xml");

        createScript();

        dropScript();
    }

    public static void createScript() throws IOException {
        final Configuration configuration = getConfiguration();

        if (configuration != null) {
            final Dialect dialect = getDialect(configuration);

            if (dialect != null) {
                final String[] lines = configuration.generateSchemaCreationScript(dialect);

                if (!ArrayUtil.isEmpty(lines)) {
                    writeScript("src/main/config/sql/create_table.sql", lines);
                }
            }
        }
    }

    public static void dropScript() throws IOException {
        final Configuration configuration = getConfiguration();

        if (configuration != null) {
            final Dialect dialect = getDialect(configuration);

            if (dialect != null) {
                final String[] lines = configuration.generateDropSchemaScript(dialect);

                if (!ArrayUtil.isEmpty(lines)) {
                    writeScript("src/main/config/sql/drop_table.sql", lines);
                }
            }
        }
    }

    private static void writeScript(String file, String[] lines)
        throws IOException {
        if (!StringUtil.isBlank(file) && !ArrayUtil.isEmpty(lines)) {
            final Formatter formatter = new DDLFormatterImpl();

            final List<String> formattedLines = new ArrayList<String>();

            for (final String line : lines) {
                String formattedLine = formatter.format(line);

                if (!StringUtil.isBlank(formattedLine)) {
                    formattedLine = formattedLine.replace("\n    ", "\n");
                    formattedLines.add(formattedLine);
                }
            }

            if (!CollectionUtil.isEmpty(formattedLines)) {
                FileUtils.writeLines(new File(file), formattedLines);
            }
        }
    }

    private static Configuration getConfiguration() {
        Configuration configuration = null;
        final EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);

        if (entityManagerFactory != null) {
            configuration = new Configuration();

            final Map<String, Object> jpaPropertyMap = entityManagerFactory.getProperties();

            if (!MapUtil.isEmpty(jpaPropertyMap)) {
                for (final Map.Entry<String, Object> entry : jpaPropertyMap.entrySet()) {
                    configuration.setProperty(entry.getKey(),
                        entry.getValue().toString());
                }
            }

            final Set<ManagedType<?>> managedTypes = entityManagerFactory.getMetamodel()
                                                                         .getManagedTypes();

            if (!CollectionUtil.isEmpty(managedTypes)) {
                for (final ManagedType<?> managedType : managedTypes) {
                    final Class<?> annotatedClass = managedType.getJavaType();
                    configuration.addAnnotatedClass(annotatedClass);
                }
            }
        }

        return configuration;
    }

    private static Dialect getDialect(Configuration configuration) {
        Dialect dialect = null;

        if (configuration != null) {
            dialect = Dialect.getDialect(configuration.getProperties());
        }

        return dialect;
    }
}
