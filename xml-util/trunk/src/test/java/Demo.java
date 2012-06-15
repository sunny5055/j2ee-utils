import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.google.code.jee.utils.xml.Address;
import com.google.code.jee.utils.xml.Company;
import com.google.code.jee.utils.xml.Employee;
import com.google.code.jee.utils.xml.XmlUtil;
import com.thoughtworks.xstream.XStream;


public class Demo {
    public static <T> void main(String[] args) throws IOException {
        XStream xStream = null;
        xStream = XmlUtil.initStream(xStream);

        Employee employee = new Employee("Robinson", "Engineer", "IT", new Address("A street", "New-York", "5498"));
        Employee employee2 = new Employee("Smith", "Engineer", "IT");
        Employee employee3 = new Employee("Johnson", "Engineer", new Address("A street", "New-York", "5498"));

        List<Employee> employees = new ArrayList<Employee>();
        employees.add(employee);
        employees.add(employee2);
        employees.add(employee3);
        
        Company company = new Company(employees, "Microsoft", 1000000);
        Company company2 = new Company(employees, "Microsoft", 1000000);

        List<Company> companies = new ArrayList<Company>();
        companies.add(company);
        companies.add(company2);
        
        Map<String, Class<T>> classNames = new HashedMap();
        classNames.put("employee", (Class<T>) employee.getClass());
        classNames.put("company", (Class<T>) company.getClass());
        
        XmlUtil.setAliases(xStream, classNames);
        
        // Instanciation d'un fichier c:/temp/article.xml
        File file = new File("src/test/resources/employee.xml");

        FileOutputStream fos = new FileOutputStream(file);
        
        //xStream.addImplicitCollection(List.class, "employees");

//        Writer writer = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
//        ObjectOutputStream outputStream = xStream.createObjectOutputStream(writer, "employees");
//        
//        xStream.toXML(employees, writer);
        
        XmlUtil.exportToXml(xStream, "data", companies, fos);
        
//        outputStream.close();
//
//        writer.close();

        fos.close();
        
      InputStream inputStream = new FileInputStream(file);
      
      List<T> importedElements = new ArrayList<T>();
              
      XmlUtil.importToXml(xStream, importedElements , inputStream);

      for(T type : importedElements) {
          System.out.println(type);
      }
        
        //XmlUtil.exportToXml(xStream, employees, fos);

    }
}
