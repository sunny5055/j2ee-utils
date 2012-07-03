import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.mail.hibernate.model.Mail;
import com.google.code.jee.utils.mail.hibernate.service.MailService;

public class Demo {
    public static void main(String[] args) {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final MailService mailService = context.getBean(MailService.class);

        Mail mail = new Mail();

        mail.setName("testMail");
        mail.setFrom("malcolm.x@gmail.com");
        mail.setReplyTo("malcolm.x@gmail.com");
        mail.setSubject("Test");
        mail.setText("This is a test !");
        mail.setHtmlMessage(false);

        mailService.create(mail);
    }
}
