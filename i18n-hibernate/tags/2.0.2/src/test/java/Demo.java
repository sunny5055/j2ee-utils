import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.jutils.bundle.hibernate.model.Label;
import com.googlecode.jutils.bundle.hibernate.model.LabelId;
import com.googlecode.jutils.bundle.hibernate.service.LabelService;

public class Demo {
    public static void main(String[] args) throws IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final LabelService labelService = context.getBean(LabelService.class);

        LabelId labelId = new LabelId();
        labelId.setKey("test");
        labelId.setLanguage("US");

        Label label = new Label();
        label.setPrimaryKey(labelId);
        label.setValue("This is a test !");

        labelService.create(label);

        final String currentDirectoryPath = new File(".").getCanonicalPath();

        OutputStream outputStream = new FileOutputStream(currentDirectoryPath
                + "/src/test/resources/bundle/text_US.properties");
        labelService.exportBundle(outputStream, "US");
    }
}
