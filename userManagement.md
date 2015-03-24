# user-management #

## Getting started ##

Define a datasource as following :

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:util="http://www.springframework.org/schema/util"
	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.2.xsd
		http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-3.2.xsd
		http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-3.2.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.2.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-3.2.xsd">

	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
		destroy-method="close">
		<property name="driverClass" value="${jdbc.driverClassName}" />
		<property name="jdbcUrl" value="${jdbc.url}" />
		<property name="user" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />
		<property name="acquireIncrement" value="${c3p0.acquire_increment}" />
		<property name="idleConnectionTestPeriod" value="${c3p0.idle_test_period}" />
		<property name="minPoolSize" value="${c3p0.min_size}" />
		<property name="maxPoolSize" value="${c3p0.max_size}" />
		<property name="loginTimeout" value="${c3p0.timeout}" />
		<property name="maxStatements" value="${c3p0.max_statements}" />
	</bean>
</beans>	
```

Define a persistence unit in META-INF/persistence.xml as following :

```
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
	version="2.0">

	<persistence-unit name="user-jpa" transaction-type="RESOURCE_LOCAL">
		<provider>org.hibernate.ejb.HibernatePersistence</provider>
	</persistence-unit>
</persistence>
```

Define a `LocalContainerEntityManagerFactoryBean` as following :

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:util="http://www.springframework.org/schema/util"
	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.2.xsd
		http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-3.2.xsd
		http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-3.2.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.2.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-3.2.xsd">

	<bean id="entityManagerFactory"
		class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="persistenceUnitName" value="user-jpa" />
		<property name="jpaProperties">
			<props>
				<prop key="hibernate.dialect">${hibernate.dialect}</prop>
				<prop key="hibernate.hbm2ddl.auto">${hibernate.hbm2ddl.auto}</prop>
				<prop key="hibernate.show_sql">${hibernate.show_sql}</prop>
			</props>
		</property>
	</bean>
</beans>	
```


Define a `JpaTransactionManager` as following :

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:util="http://www.springframework.org/schema/util"
	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.2.xsd
		http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-3.2.xsd
		http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-3.2.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.2.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-3.2.xsd">

	<bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
		<property name="entityManagerFactory" ref="entityManagerFactory" />
		<property name="nestedTransactionAllowed" value="true" />
	</bean>
</beans>	
```

After you've finished the Spring configuration, you must now define a `database.properties`, which is used by the Spring configuration file.
Here is an example of a `PostgreSQL` connection with `Hibernate` abd `c3po`.

```
# Jdbc
jdbc.driverClassName=org.postgresql.Driver
jdbc.maxActive=100
jdbc.maxWait=1000
jdbc.url=jdbc:postgresql://localhost:5432/user-jpa
jdbc.username=postgres
jdbc.password=postgres

# Hibernate
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.show_sql=false
hibernate.hbm2ddl.auto=update

# c3p0
c3p0.acquire_increment=1
c3p0.idle_test_period=100
c3p0.min_size=10
c3p0.max_size=100
c3p0.timeout=100
c3p0.max_statements=0
```

Use the component as follows :

```
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
			rights.add(rightService.findByCode("EDIT_ALL"));
			rights.add(rightService.findByCode("DELETE_ALL"));
			rights.add(rightService.findByCode("READ_ALL"));
		} else if (role.getCode().equals("REDACTOR")) {
			rights.add(rightService.findByCode("WRITE"));
			rights.add(rightService.findByCode("EDIT"));
			rights.add(rightService.findByCode("DELETE"));
			rights.add(rightService.findByCode("READ"));
		} else if (role.getCode().equals("CONTRIBUTOR")) {
			rights.add(rightService.findByCode("READ_ALL"));
		} else if (role.getCode().equals("MEMBER")) {
			rights.add(rightService.findByCode("READ"));
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
		user.setActive(true);
		List<Role> roles = new ArrayList<Role>();
		if (user.getFirstName().equals("Nicolas")) {
			roles.add(roleService.findByCode("MEMBER"));
		} else if (user.getFirstName().equals("Jérome")) {
			roles.add(roleService.findByCode("MEMBER"));
		} else if (user.getFirstName().equals("Robert")) {
			roles.add(roleService.findByCode("ADMINISTRATOR"));
		} else if (user.getFirstName().equals("Nathan")) {
			roles.add(roleService.findByCode("REDACTOR"));
		} else if (user.getFirstName().equals("Justine")) {
			roles.add(roleService.findByCode("REDACTOR"));
			roles.add(roleService.findByCode("CONTRIBUTOR"));
		} else if (user.getFirstName().equals("Jordan")) {
			roles.add(roleService.findByCode("REDACTOR"));
		} else if (user.getFirstName().equals("Julie")) {
			roles.add(roleService.findByCode("GUESS"));
		} else if (user.getFirstName().equals("Sophie")) {
			roles.add(roleService.findByCode("MODERATOR"));
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
		List<Right> rights = rightService.findAllByRoleId(currentRole.getId());
		for (Right currentRight : rights) {
			buffer.append("\t\t" + currentRight.getCode() + "\n");
		}
	}
	
	// Print the informations
	System.out.println(buffer);        
}
```