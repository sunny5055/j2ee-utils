package com.google.code.jee.utils.ms.project.file.management.service;

import java.io.IOException;
import java.io.InputStream;

import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;

import com.google.code.jee.utils.ms.project.file.management.model.MSProject;

/**
 * The Interface MSProjectService.
 */
public interface MSProjectService {
	
	/**
	 * Import a MS project file (MPP, MPX, MSPDI)
	 * @param input the path of the MS file
	 * @return an instance of MSProject
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws MPXJException
	 * @throws IOException 
	 */
	MSProject importProject(InputStream input)
			throws InstantiationException, IllegalAccessException, MPXJException, IOException;
	
	/**
	 * Export a MSProject object to a ProjectFile object (MPXJ API)
	 * @param msProject an instance of MSProject
	 * @return an instance of ProjectFile (MPXJ API)
	 */
	ProjectFile exportProject(MSProject msProject);
	
	/**
	 * Export a MSProject object to a XML file (MSPDI)
	 * @param msProject an instance of MSProject
	 * @param input the destination where the MSPDI file will be created
	 * @throws IOException 
	 */
	void exportProjectToMSPDIFile(MSProject msProject,
			InputStream input) throws IOException;
	
}
