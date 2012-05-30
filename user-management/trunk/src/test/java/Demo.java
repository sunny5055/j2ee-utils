import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.user.management.model.Right;
import com.google.code.jee.utils.user.management.model.Role;
import com.google.code.jee.utils.user.management.model.User;
import com.google.code.jee.utils.user.management.service.UserService;

public class Demo {
	
    public static void main(String[] args) {
    	
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        Security.addProvider(new BouncyCastleProvider());
        final UserService userService = context.getBean(UserService.class);

        // Create an user with one role and three rights
        User user = new User();
        user.setFirstName("Malcom");
        user.setLastName("X");
        user.setLogin("login");
        user.setPassword("hello");
        user.setMail("malcom.x@gmail.com");
        
        Right delete = new Right();
        delete.setName("Delete");
        Right insert = new Right();
        insert.setName("Insert");
        Right edit = new Right();
        edit.setName("Edit");
        
        List<Right> rights = new ArrayList<Right>();
        rights.add(delete);
        rights.add(insert);
        rights.add(edit);
        
        Role administrator = new Role();
        administrator.setName("Administrator");
        administrator.setRights(rights);
        
        List<Role> roles = new ArrayList<Role>();
        roles.add(administrator);
        user.setRoles(roles);
        
        userService.create(user);
        user.setLogin(user.getFirstName() + user.getId());
        userService.update(user);
        
        StringBuffer buffer = new StringBuffer();
        
        // Print all users
        List<User> users = userService.findAll();
        buffer.append("Users :\n");
        for (User currentUser : users) {
        	buffer.append("\t"  + currentUser + "\n");
        	// Print his roles
        	List<Role> userRoles = userService.findAllByUserId(currentUser.getId());
        	buffer.append("His roles :\n");
        	for (Role currentRole : userRoles ) {
        		buffer.append("\t" + currentRole + "\n");
        		// Print his rights for current role
        		List<Right> roleRights = userService.findAllByRoleId(currentRole.getId());
        		buffer.append("His rights with the current Role :\n");
        		for (Right currentRight : roleRights)
        			buffer.append("\t" + currentRight + "\n");
        	}
        }
        
        // Print all roles
        List<Role> allRoles = userService.findAllRoles();
        buffer.append("\nAll roles :\n");
        for (Role currentRole : allRoles)
        	buffer.append("\t" + currentRole + "\n");
        
        // Print all rights
        List<Right> allRights = userService.findAllRights();
        buffer.append("All rights :\n");
        for (Right currentRight : allRights)
        	buffer.append("\t" + currentRight + "\n");
        
        // Delete the user, his roles and his rights
        //userService.delete(user);
        
        // Print the buffer
        System.out.println(buffer);
        
    }
    
}
