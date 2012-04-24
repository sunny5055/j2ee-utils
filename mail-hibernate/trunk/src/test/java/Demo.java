import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo {
    public static void main(String[] args) throws IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");

        final DataSource dataSource = context.getBean(DataSource.class);
        System.out.println();

        // final MailService mailService = context.getBean(MailService.class);
        //
        // final List<Mail> mails = mailService.findAll();
        // if (!CollectionUtil.isEmpty(mails)) {
        // for (final Mail mail : mails) {
        // System.out.println(mail.getName());
        // }
        // }
    }
}
