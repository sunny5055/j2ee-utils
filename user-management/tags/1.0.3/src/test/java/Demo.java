import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.code.jee.utils.user.management.model.Right;
import com.google.code.jee.utils.user.management.model.Role;
import com.google.code.jee.utils.user.management.model.User;
import com.google.code.jee.utils.user.management.service.RightService;
import com.google.code.jee.utils.user.management.service.RoleService;
import com.google.code.jee.utils.user.management.service.UserService;

public class Demo {
	
    public static void main(String[] args) {
    	
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
        final UserService userService = context.getBean(UserService.class);
        final RoleService roleService = context.getBean(RoleService.class);
        final RightService rightService = context.getBean(RightService.class);

        // Instantiates arrays of informations about users 
        String[] firstNames = { "Nicolas", "Jérome", "Robert", "Nathan", "Justine",
        		"Jordan", "Julie", "Sophie", "Hacker" };
        String[] lastNames = { "Dupont", "Meyer", "Durand", "Lefebvre", "Lopez",
        		"Perrin", "Lerroy", "Chevalier", "Hacker" };
        String[] logins = { "n.dupont54", "j.meyer22", "r.durand89" ,"n.lefebvre78", 
        		"j.lopez54", "j.perrin12", "j.lerroy93", "s.chevalier36", "H@ck3r" };
        String[] passwords = { "38Sdsqd", "sflj2", "s234F", "37HDd", "93jdsklqd",
        		"dfj9", "hedo32", "DDKML3","123" };
        String[] mails = { "nico.dupont@gmail.com", "j.meyer@wanadoo.fr", "durand.robert@systemDco.eu", 
        		"nathan@yahoo.fr", "lopez@etu-univnancy2.fr", "jordan_perrin@free.fr", "julie93@hotmail.com",
        		"sophie.chevalier@systemDco.eu", "hello@world.java" };
        
        // Instantiates arrays of informations about roles
        String[] roleCodes = { "ADMINISTRATOR", "MODERATOR", "REDACTOR", "CONTRIBUTOR", "MEMBER", "GUESS" };
        String[] roleDescriptions = { "The administrator of the application", "A special user who moderates the contributions of others users",
        		"An user who publishes articles", "A valuable user because of his contributions", "A simple user", "A guess (default role)" };
        
        // Instantiates arrays of informations about rights
        String[] rightCodes = { "WRITE", "EDIT", "EDIT_ALL", "DELETE", "DELETE_ALL", "READ", "READ_ALL" };
        String[] rightDescriptions = { "Is allowed to write an article", "Is allowed to edit his articles", "Is allowed to edit all articles",
        		"Is allowed to delete his articles", "Is allowed to delete all articles", "Is allowed to read public articles",
        		"Is allowed to read all articles" };
        
        // Create rights
        for (int i = 0 ; i < 7 ; i++) {
        	Right right = new Right();
        	right.setCode(rightCodes[i]);
        	right.setDescription(rightDescriptions[i]);
        	rightService.create(right);
        }
        
        // Create roles and binds them to rights
        for (int i = 0 ; i < 6 ; i++) {
        	Role role = new Role();
        	role.setCode(roleCodes[i]);
        	role.setDescription(roleDescriptions[i]);
        	List<Right> rights = new ArrayList<Right>();
        	if (role.getCode().equals("ADMINISTRATOR")) {
        		rights.addAll(rightService.findAll());
        	} else if (role.getCode().equals("MODERATOR")) {
        		rights.add(rightService.findByRightCode("EDIT"));
        		rights.add(rightService.findByRightCode("EDIT_ALL"));
        		rights.add(rightService.findByRightCode("DELETE"));
        		rights.add(rightService.findByRightCode("DELETE_ALL"));
        		rights.add(rightService.findByRightCode("READ"));
        		rights.add(rightService.findByRightCode("READ_ALL"));
        	} else if (role.getCode().equals("REDACTOR")) {
        		rights.add(rightService.findByRightCode("WRITE"));
        		rights.add(rightService.findByRightCode("EDIT"));
        		rights.add(rightService.findByRightCode("DELETE"));
        		rights.add(rightService.findByRightCode("READ"));
        	} else if (role.getCode().equals("CONTRIBUTOR")) {
        		rights.add(rightService.findByRightCode("READ_ALL"));
        	} else if (role.getCode().equals("MEMBER")) {
        		rights.add(rightService.findByRightCode("READ"));
        	}
        	role.setRights(rights);
        	roleService.create(role);
        }
        
        // Create users and binds them to roles
        for (int i = 0 ; i < 9 ; i++) {
        	User user = new User();
        	user.setFirstName(firstNames[i]);
        	user.setLastName(lastNames[i]);
        	user.setLogin(logins[i]);
        	user.setPassword(passwords[i]);
        	user.setMail(mails[i]);
        	List<Role> roles = new ArrayList<Role>();
        	if (user.getFirstName().equals("Nicolas")) {
        		roles.add(roleService.findByRoleCode("MEMBER"));
        	} else if (user.getFirstName().equals("Jérome")) {
        		roles.add(roleService.findByRoleCode("MEMBER"));
        	} else if (user.getFirstName().equals("Robert")) {
        		roles.add(roleService.findByRoleCode("ADMINISTRATOR"));
        	} else if (user.getFirstName().equals("Nathan")) {
        		roles.add(roleService.findByRoleCode("REDACTOR"));
        	} else if (user.getFirstName().equals("Justine")) {
        		roles.add(roleService.findByRoleCode("REDACTOR"));
        		roles.add(roleService.findByRoleCode("CONTRIBUTOR"));
        	} else if (user.getFirstName().equals("Jordan")) {
        		roles.add(roleService.findByRoleCode("REDACTOR"));
        	} else if (user.getFirstName().equals("Julie")) {
        		roles.add(roleService.findByRoleCode("GUESS"));
        	} else if (user.getFirstName().equals("Sophie")) {
        		roles.add(roleService.findByRoleCode("MODERATOR"));
        	}
        	user.setRoles(roles);
        	userService.create(user);
        }
        
        // Get informations about an user
        StringBuffer buffer = new StringBuffer();
        User justine = userService.findByLogin("j.lopez54");
        buffer.append(justine.getFirstName() + " :\n");
        List<Role> justineRoles = roleService.findAllByUserId(justine.getId());
        for (Role currentRole : justineRoles) {
        	buffer.append("\t" + currentRole.getCode() + " :\n");
        	List<Right> rights = rightService.findAllRightsByRoleId(currentRole.getId());
        	for (Right currentRight : rights) {
        		buffer.append("\t\t" + currentRight.getCode() + "\n");
        	}
        }
        
        // Print the informations
        System.out.println(buffer);
    }
    
}
