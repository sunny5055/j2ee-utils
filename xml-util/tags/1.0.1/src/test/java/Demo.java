import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.code.jee.utils.xml.Address;
import com.google.code.jee.utils.xml.Company;
import com.google.code.jee.utils.xml.Employee;
import com.google.code.jee.utils.xml.User;
import com.google.code.jee.utils.xml.XmlUtil;
import com.thoughtworks.xstream.XStream;

public class Demo {
    public static <T> void main(String[] args) throws IOException {
        // Inits the stream
        final XStream xStream = XmlUtil.getStream();

        // Creates the employees
        final Employee employee = new Employee("Robinson", "Engineer", "IT",
                new Address("A street", "New-York", "5498"));
        final Employee employee2 = new Employee("Smith", "Engineer", "IT");
        final Employee employee3 = new Employee("Johnson", "Engineer", new Address("A street", "New-York", "5498"));

        final List<Employee> employees = new ArrayList<Employee>();
        employees.add(employee);
        employees.add(employee2);
        employees.add(employee3);

        // Creates the companies
        final Company company = new Company(employees, "Microsoft", 1000000);
        final Company company2 = new Company(employees, "Microsoft2", 1000000);

        final List<Company> companies = new ArrayList<Company>();
        companies.add(company);
        companies.add(company2);

        // Creates the users
        final User user = new User("robinson", "password");
        final User user2 = new User("robinson", "password");

        XmlUtil.addAliasForClass(xStream, Employee.class);
        XmlUtil.addAliasForClass(xStream, Company.class);
        XmlUtil.addAliasForClass(xStream, User.class);

        // Set fields as attributes
        XmlUtil.setAttributesFor(xStream, Employee.class, "name", "designation");
        XmlUtil.setAttributesFor(xStream, Company.class, "name");

        // Omits fields
        XmlUtil.omitFields(xStream, Employee.class, "department");
        XmlUtil.omitFields(xStream, Company.class, "incomes");

        // Creates a new XML file
        final File file = new File("src/test/resources/employee.xml");

        final FileOutputStream fos = new FileOutputStream(file);

        // Export all the elements
        final List<Object> elements = new ArrayList<Object>();
        elements.add(company);
        // elements.add((T) company2);
        elements.add(user);
        elements.add(user2);
        XmlUtil.exportToXml(xStream, fos, elements);

        fos.close();

        // Import the elements
        final InputStream inputStream = new FileInputStream(file);

        final List<?> importedElements = XmlUtil.importToXml(xStream, inputStream);

        for (final Object type : importedElements) {
            System.out.println(type);
        }
    }
}
