import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.jutils.test.model.User;
import com.googlecode.jutils.test.service.UserService;

public class Main {
	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(new String[] { "spring/application-context.xml" });

		test();
	}

	private static void test() {
		final UserService userService = context.getBean(UserService.class);
		if (userService != null) {
			User user = createUser("Jérémy", "SCHAAL");
			user = userService.create(user);
		}
	}

	private static User createUser(String name, String lastname) {
		final User user = new User();
		user.setName(name);
		user.setLastname(lastname);
		return user;
	}
}
