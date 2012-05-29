import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.user.management.model.User;
import com.google.code.jee.utils.user.management.service.UserService;

public class Demo {
    public static void main(String[] args) {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final UserService userService = context.getBean(UserService.class);

        User user = new User();

        user.setFirstName("Malcom");
        user.setLastName("X");
        user.setLogin(user.getLastName() + user.getId());
        user.setMail("malcom.x@gmail.com");
        userService.create(user);
    }
}
