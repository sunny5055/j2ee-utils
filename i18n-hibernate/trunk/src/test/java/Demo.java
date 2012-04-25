import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.bundle.hibernate.service.LabelService;

public class Demo {
    public static void main(String[] args) throws IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final LabelService labelService = context.getBean(LabelService.class);

        FileInputStream fileInputStream = null;
        final String currentDirectoryPath = new File(".").getCanonicalPath();
        try {
            fileInputStream = new FileInputStream(currentDirectoryPath + "/src/test/resources/bundle/text_US.properties");
        } catch (final FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        labelService.importBundle(fileInputStream, "US");
        System.out.println();

        //
        // final List<Mail> mails = mailService.findAll();
        // if (!CollectionUtil.isEmpty(mails)) {
        // for (final Mail mail : mails) {
        // System.out.println(mail.getName());
        // }
        // }
    }
}
