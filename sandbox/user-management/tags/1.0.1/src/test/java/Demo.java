import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.user.management.model.Right;
import com.google.code.jee.utils.user.management.model.Role;
import com.google.code.jee.utils.user.management.model.User;
import com.google.code.jee.utils.user.management.service.RoleService;
import com.google.code.jee.utils.user.management.service.UserService;

public class Demo {
	
    public static void main(String[] args) {
    	
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final UserService userService = context.getBean(UserService.class);
        final RoleService roleService = context.getBean(RoleService.class);

        // Create an user with one role and three rights
        User user = new User();
        user.setFirstName("Malcom");
        user.setLastName("X");
        user.setLogin("login");
        user.setPassword("hello");
        user.setMail("malcom.x@gmail.com");
        
        Right delete = new Right();
        delete.setCode("DELETE");
        delete.setDescription("Allows to delete");
        Right insert = new Right();
        insert.setCode("INSERT");
        insert.setDescription("Allows to insert");
        Right edit = new Right();
        edit.setCode("EDIT");
        edit.setDescription("Allows to edit");
        
        List<Right> rights = new ArrayList<Right>();
        rights.add(delete);
        rights.add(insert);
        rights.add(edit);
        
        Role administrator = new Role();
        administrator.setCode("ADMIN");
        administrator.setDescription("The administrator of the system");
        administrator.setRights(rights);
        
        List<Role> roles = new ArrayList<Role>();
        roles.add(administrator);
        user.setRoles(roles);
        
        // Insert the user, his roles and his rights
        userService.create(user);
        user.setLogin(user.getFirstName() + user.getId());
        userService.update(user);
        
        // Print a the roles of the user
        List<Role> userRoles = roleService.findAllByUserId(user.getId());
        for (Role currentRole : userRoles)
        	System.out.println(currentRole.getCode());
        
        // Delete the user, his roles and his rights
        userService.delete(user);
        
    }
    
}
