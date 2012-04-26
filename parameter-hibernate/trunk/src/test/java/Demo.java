import java.io.IOException;
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

        final IntegerParameter integerParameter = new IntegerParameter();
        final StringParameter stringParameter = new StringParameter();
        final BooleanParameter booleanParameter = new BooleanParameter();
        final DateParameter dateParameter = new DateParameter();
        final FloatParameter floatParameter = new FloatParameter();

        integerParameter.setName("integer");
        floatParameter.setName("float");
        dateParameter.setName("date");
        booleanParameter.setName("boolean");
        stringParameter.setName("string");

        integerParameter.setValue(5020);
        floatParameter.setValue(2.F);
        dateParameter.setValue(new Date());
        booleanParameter.setValue(true);
        stringParameter.setValue("A test string");

        parameterService.setValue(integerParameter.getName(), integerParameter.getValue());
        parameterService.setValue(floatParameter.getName(), floatParameter.getValue());
        parameterService.setValue(dateParameter.getName(), dateParameter.getValue());
        parameterService.setValue(booleanParameter.getName(), booleanParameter.getValue());
        parameterService.setValue(stringParameter.getName(), stringParameter.getValue());

        parameterService.removeValue(integerParameter.getName());
        parameterService.removeValue(floatParameter.getName());
        parameterService.removeValue(dateParameter.getName());
        parameterService.removeValue(booleanParameter.getName());
        parameterService.removeValue(stringParameter.getName());

        System.err.println(parameterService.getValue(integerParameter.getName()).toString());
        System.err.println(parameterService.getValue(floatParameter.getName()).toString());
        System.err.println(parameterService.getValue(dateParameter.getName()).toString());
        System.err.println(parameterService.getValue(booleanParameter.getName()).toString());
        System.err.println(parameterService.getValue(stringParameter.getName()));

        System.out.println(parameterService.count());

        for (AbstractParameter<?> param : parameterService.findAll()) {
            System.out.println(param.getName());
            System.out.println(param.getType());

        }

        System.out.println(parameterService.existWithName("test"));
    }
}
