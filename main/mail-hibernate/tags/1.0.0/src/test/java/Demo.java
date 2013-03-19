import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo {
    public static void main(String[] args) throws IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        // final MailService mailService = context.getBean(MailService.class);

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
