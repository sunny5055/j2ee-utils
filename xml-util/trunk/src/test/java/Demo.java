import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.jee.utils.xml.Address;
import com.google.code.jee.utils.xml.Company;
import com.google.code.jee.utils.xml.Employee;
import com.google.code.jee.utils.xml.User;
import com.google.code.jee.utils.xml.XmlUtil;
import com.thoughtworks.xstream.XStream;

public class Demo {
    public static <T> void main(String[] args) throws IOException {
        // Inits the stream
        XStream xStream = null;
        xStream = XmlUtil.initStream(xStream);

        // Creates the employees
        Employee employee = new Employee("Robinson", "Engineer", "IT", new Address("A street", "New-York", "5498"));
        Employee employee2 = new Employee("Smith", "Engineer", "IT");
        Employee employee3 = new Employee("Johnson", "Engineer", new Address("A street", "New-York", "5498"));

        List<Employee> employees = new ArrayList<Employee>();
        employees.add(employee);
        employees.add(employee2);
        employees.add(employee3);

        // Creates the companies
        Company company = new Company(employees, "Microsoft", 1000000);
        Company company2 = new Company(employees, "Microsoft2", 1000000);

        List<Company> companies = new ArrayList<Company>();
        companies.add(company);
        companies.add(company2);
        
        // Creates the users
        User user = new User("robinson", "password");
        User user2 = new User("robinson", "password");
        
        // Sets the aliases
        Map<String, Class<?>> classNames = new HashMap<String, Class<?>>();

        classNames.put("employee", employee.getClass());
        classNames.put("company", company.getClass());
        classNames.put("user", user.getClass());

        XmlUtil.setAliases(xStream, classNames);
        
        // Set fields as attributes
        Map<Class<?>, List<String>> attributeNames = new HashMap<Class<?>, List<String>>();
        List<String> employeeAttributes = new ArrayList<String>();
        employeeAttributes.add("name");
        employeeAttributes.add("designation");
        attributeNames.put(employee.getClass(), employeeAttributes);
        List<String> companyAttributes = new ArrayList<String>();
        companyAttributes.add("name");
        attributeNames.put(company.getClass(), companyAttributes);
        XmlUtil.setAttributesFor(xStream, attributeNames);
        
        // Omits fields
        Map<Class<?>, List<String>> fieldNames = new HashMap<Class<?>, List<String>>();
        List<String> employeeFields = new ArrayList<String>();
        employeeFields.add("department");
        fieldNames.put(employee.getClass(), employeeFields);
        List<String> companyFields = new ArrayList<String>();
        companyFields.add("incomes");
        fieldNames.put(company.getClass(), companyFields);

        XmlUtil.omitFields(xStream, fieldNames);
        
        // Creates a new XML file
        File file = new File("src/test/resources/employee.xml");

        FileOutputStream fos = new FileOutputStream(file);

        // Export all the elements
        List<T> elements = new ArrayList();
        elements.add((T) company);
        //elements.add((T) company2);
        elements.add((T) user);
        elements.add((T) user2);
        XmlUtil.exportToXml(xStream, "data", "elements", elements, fos);

        fos.close();

        // Import the elements
        InputStream inputStream = new FileInputStream(file);

        List<?> importedElements = XmlUtil.importToXml(xStream, inputStream);

        for (Object type : importedElements) {
            System.out.println(type);
        }
    }
}
