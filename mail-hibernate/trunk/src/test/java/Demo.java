
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.mail.hibernate.dao.MailAttachmentDao;
import com.google.code.jee.utils.mail.hibernate.dao.MailDao;
import com.google.code.jee.utils.mail.hibernate.model.Mail;


public class Demo {
    public static void main(String[] args) throws IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final MailAttachmentDao mailAttachmentDao = context.getBean(MailAttachmentDao.class);
        final MailDao mailDao = context.getBean(MailDao.class);
        
        for(Mail mail : mailDao.findAll()) {
            System.out.println(mail.getName());
        }
    }
}
