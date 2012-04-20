import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.mail.exception.MailSenderServiceException;
import com.google.code.jee.utils.mail.service.MailSenderService;

public class Demo {
    public static void main(String[] args) throws IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final MailSenderService mailService = context.getBean(MailSenderService.class);

        final List<String> recipients = new ArrayList<String>();
        recipients.add("m.assem@technologyandstrategy.com");

        final List<String> carbonCopies = new ArrayList<String>();
        carbonCopies.add("mehdi.assem@gmail.com");

        final List<String> blindCarbonCopies = new ArrayList<String>();
        blindCarbonCopies.add("mehdi.assem@gmail.com");

        final Map<String, InputStream> inputStreams = new HashMap<String, InputStream>();
        FileInputStream file = null;
        final String currentDirectoryPath = new File(".").getCanonicalPath();
        try {
            file = new FileInputStream(currentDirectoryPath + "/src/test/resources/image/logo_t&s.png");
        } catch (final FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        inputStreams.put("logo_t&s.png", file);

        try {
            mailService.sendMail("mehdi.assem@gmail.com", recipients,
                    carbonCopies, blindCarbonCopies, "", "<html><head></head><body><h1>text</h1></body></html>", true, inputStreams);
        } catch (final MailSenderServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
