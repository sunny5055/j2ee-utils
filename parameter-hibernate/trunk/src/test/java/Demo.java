import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.jutils.parameter.hibernate.enumeration.DateFormatEnum;
import com.googlecode.jutils.parameter.hibernate.model.AbstractParameter;
import com.googlecode.jutils.parameter.hibernate.service.ParameterService;

public class Demo {
    private static final Logger LOGGER = Logger.getLogger(Demo.class);

    public static void main(String[] args) throws IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final ParameterService parameterService = context.getBean(ParameterService.class);

        // Test all values removal
        parameterService.removeAllValues();

        // setValue method call
        parameterService.setValue("integerTest", "An integer parameter", 5020);
        parameterService.setValue("floatTest", "A float parameter", 2.F);
        parameterService.setValue("dateTest", "A date parameter", new Date(), DateFormatEnum.DATE_ONLY.getValue());
        parameterService.setValue("timeTest", "A time parameter", new Date(), DateFormatEnum.TIME_ONLY.getValue());
        parameterService.setValue("dateTimeTest", "A date time parameter", new Date(),
                DateFormatEnum.DATE_TIME.getValue());
        parameterService.setValue("booleanTest", "A boolean parameter", true);
        parameterService.setValue("stringTest", "A string parameter", "A test string");

        // getValue test
        LOGGER.debug("integerTest : " + parameterService.getValue("integerTest"));
        LOGGER.debug("floatTest : " + parameterService.getValue("floatTest"));
        LOGGER.debug("dateTest : " + parameterService.getValue("dateTest"));
        LOGGER.debug("timeTest : " + parameterService.getValue("timeTest"));
        LOGGER.debug("dateTimeTest : " + parameterService.getValue("dateTimeTest"));
        LOGGER.debug("booleanTest : " + parameterService.getValue("booleanTest"));
        LOGGER.debug("stringTest : " + parameterService.getValue("stringTest"));

        // count test
        LOGGER.debug(parameterService.count());

        // exists test
        final AbstractParameter<?> parameter = parameterService.findByName("integerTest");
        LOGGER.debug(parameter.getName());
        LOGGER.debug(parameter.getValue());
        LOGGER.debug(parameter.getType());
        LOGGER.debug(parameter.getDescription());

        // findAll listing
        for (final AbstractParameter<?> param : parameterService.findAll()) {
            LOGGER.debug(param.getName() + " - " + param.getType() + " : " + param.getDescription());
        }

        // Test existsWithName
        LOGGER.debug(parameterService.existWithName("test"));

        final String currentDirectoryPath = new File(".").getCanonicalPath();

        // export properties
        final OutputStream outputStream = new FileOutputStream(currentDirectoryPath
                + "/src/test/resources/properties/parameters.properties");
        parameterService.exportProperties(outputStream);

        // parameterService.removeAllValues();

        // // import values into the database
        // FileInputStream fileInputStream = null;
        // try {
        // fileInputStream = new FileInputStream(currentDirectoryPath
        // + "/src/test/resources/properties/parameters.properties");
        // } catch (final FileNotFoundException e1) {
        // e1.printStackTrace();
        // }
        // parameterService.importProperties(fileInputStream);
    }
}
