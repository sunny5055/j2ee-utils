import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.mail.exception.MailServiceException;
import com.google.code.jee.utils.mail.service.MailService;

public class Demo {
    public static void main(String[] args) throws IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final MailService mailService = context.getBean(MailService.class);

        final List<String> recipients = new ArrayList<String>();
        recipients.add("m.assem@technologyandstrategy.com");

        final List<String> carbonCopies = new ArrayList<String>();
        carbonCopies.add("mehdi.assem@gmail.com");

        final List<String> blindCarbonCopies = new ArrayList<String>();
        blindCarbonCopies.add("mehdi.assem@gmail.com");

        final List<InputStream> inputStreams = new ArrayList<InputStream>();
        FileInputStream file = null;
        final String currentDirectoryPath = new File(".").getCanonicalPath();
        try {
            file = new FileInputStream(currentDirectoryPath + "/src/test/resources/image/logo_t&s.png");
        } catch (final FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        inputStreams.add(file);

        try {
            mailService.sendMail("mehdi.assem@gmail.com", "m.assem@technologyandstrategy.com", recipients,
                    carbonCopies, blindCarbonCopies, "", "text", true, inputStreams);
        } catch (final MailServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
