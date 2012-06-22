import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.parameter.hibernate.model.AbstractParameter;
import com.google.code.jee.utils.parameter.hibernate.model.BooleanParameter;
import com.google.code.jee.utils.parameter.hibernate.model.DateParameter;
import com.google.code.jee.utils.parameter.hibernate.model.FloatParameter;
import com.google.code.jee.utils.parameter.hibernate.model.IntegerParameter;
import com.google.code.jee.utils.parameter.hibernate.model.StringParameter;
import com.google.code.jee.utils.parameter.hibernate.service.ParameterService;

public class Demo {
    public static void main(String[] args) throws IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final ParameterService parameterService = context.getBean(ParameterService.class);

        // Test all values removal
        parameterService.removeAllValues();

        // Creation of values of all types
        final IntegerParameter integerParameter = new IntegerParameter();
        final StringParameter stringParameter = new StringParameter();
        final BooleanParameter booleanParameter = new BooleanParameter();
        final DateParameter dateParameter = new DateParameter();
        final FloatParameter floatParameter = new FloatParameter();

        // Setting names
        integerParameter.setName("integerTest");
        floatParameter.setName("floatTest");
        dateParameter.setName("dateTest");
        booleanParameter.setName("booleanTest");
        stringParameter.setName("stringTest");
        
        // Setting descriptions
        integerParameter.setDescription("An integer parameter");
        floatParameter.setDescription("A float parameter");
        dateParameter.setDescription("A date parameter");
        booleanParameter.setDescription("A boolean parameter");
        stringParameter.setDescription("A string parameter");
        
        // Setting values0
        integerParameter.setValue(5020);
        floatParameter.setValue(2.F);
        dateParameter.setValue(new Date());
        booleanParameter.setValue(true);
        stringParameter.setValue("A test string");

        // setValue method call
        parameterService.setValue(integerParameter.getName(), integerParameter.getDescription(), integerParameter.getValue());
        parameterService.setValue(floatParameter.getName(), floatParameter.getDescription(), floatParameter.getValue());
        parameterService.setValue(dateParameter.getName(), dateParameter.getDescription(), dateParameter.getValue());
        parameterService.setValue(booleanParameter.getName(), booleanParameter.getDescription(), booleanParameter.getValue());
        parameterService.setValue(stringParameter.getName(), stringParameter.getDescription(), stringParameter.getValue());

        // getValue test
        System.err.println(parameterService.getValue(integerParameter.getName()).toString());
        System.err.println(parameterService.getValue(floatParameter.getName()).toString());
        System.err.println(parameterService.getValue(dateParameter.getName()).toString());
        System.err.println(parameterService.getValue(booleanParameter.getName()).toString());
        System.err.println(parameterService.getValue(stringParameter.getName()));

        // count test
        System.out.println(parameterService.count());

        //exists test
        AbstractParameter<?> parameter = parameterService.findByName("integerTest");
        System.out.println(parameter.getName());
        System.out.println(parameter.getValue());
        System.out.println(parameter.getType());
        System.out.println(parameter.getDescription());
        
        // findAll listing
        for (AbstractParameter<?> param : parameterService.findAll()) {
            System.out.println(param.getName());
            System.out.println(param.getType());
            System.out.println(param.getDescription());
        }

        // Test existsWithName
        System.out.println(parameterService.existWithName("test"));

        final String currentDirectoryPath = new File(".").getCanonicalPath();

        // export properties
        OutputStream outputStream = new FileOutputStream(currentDirectoryPath
                + "/src/test/resources/properties/parameters.properties");
        parameterService.exportProperties(outputStream);

        parameterService.removeAllValues();

        // import values into the database
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(currentDirectoryPath
                    + "/src/test/resources/properties/parameters.properties");
        } catch (final FileNotFoundException e1) {
            e1.printStackTrace();
        }
        parameterService.importProperties(fileInputStream);
    }
}
