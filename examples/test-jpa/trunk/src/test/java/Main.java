import java.util.Arrays;
import java.util.List;

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
			Integer count = null;
			List<User> users = null;
			User user = null;

			user = createUser("Jérémy", "SCHAAL");
			final Integer pk = userService.create(user);
			System.out.println("create : " + pk);

			count = userService.count();
			System.out.println("count : " + count);

			users = userService.getObjects(Arrays.asList(1, 2));
			System.out.println("getObjects : " + users.size());

			users = userService.findAll();
			System.out.println("findAll : " + users.size());

			final boolean exist = userService.existPk(1);
			System.out.println("existPk : " + exist);
		}
	}

	private static User createUser(String name, String lastname) {
		final User user = new User();
		user.setName(name);
		user.setLastname(lastname);
		return user;
	}
}
