import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.mpxj.MPXJException;

import com.google.code.jee.utils.ms.project.file.management.model.MSProject;
import com.google.code.jee.utils.ms.project.file.management.service.MSProjectService;
import com.google.code.jee.utils.ms.project.file.management.service.impl.MSProjectServiceImpl;

public class Demo {
	
    public static void main(String[] args) throws MPXJException, InstantiationException, IllegalAccessException, IOException {
    	MSProjectService msProjectService = new MSProjectServiceImpl();

    	String filePath = "C:\\Users\\j.neuhart\\Desktop\\CRLorraine_Planning.mpp";
    	InputStream inputFilePath = new ByteArrayInputStream(filePath.getBytes());
    	
    	String destinationPath = "C:\\Users\\j.neuhart\\Desktop\\";
    	InputStream inputDestinationPath = new ByteArrayInputStream(destinationPath.getBytes());
    	
    	MSProject msProject = msProjectService.importProject(inputFilePath);       	
    	msProjectService.exportProjectToMSPDIFile(msProject, inputDestinationPath);
		System.out.println("Project imported and exported !");
    }
    
}
