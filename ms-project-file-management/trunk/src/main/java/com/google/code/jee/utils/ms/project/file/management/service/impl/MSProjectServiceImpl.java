package com.google.code.jee.utils.ms.project.file.management.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import net.sf.mpxj.ConstraintType;
import net.sf.mpxj.Duration;
import net.sf.mpxj.MPXJException;
import net.sf.mpxj.Priority;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectHeader;
import net.sf.mpxj.Relation;
import net.sf.mpxj.RelationType;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.ResourceType;
import net.sf.mpxj.Task;
import net.sf.mpxj.TimeUnit;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.reader.ProjectReaderUtility;

import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.ms.project.file.management.model.MSDuration;
import com.google.code.jee.utils.ms.project.file.management.model.MSProject;
import com.google.code.jee.utils.ms.project.file.management.model.MSRelation;
import com.google.code.jee.utils.ms.project.file.management.model.MSResource;
import com.google.code.jee.utils.ms.project.file.management.model.MSResourceAssignment;
import com.google.code.jee.utils.ms.project.file.management.model.MSTask;
import com.google.code.jee.utils.ms.project.file.management.service.MSProjectService;

public class MSProjectServiceImpl implements MSProjectService {
	
	/**
     * {@inheritedDoc}
     */
    @Override
	public MSProject importProject(InputStream input) throws InstantiationException, IllegalAccessException, MPXJException, IOException {
    	String filePath = IOUtils.toString(input);
    	// Instantiates a new MSProject 	
        ProjectReader reader = ProjectReaderUtility.getProjectReader(filePath);
        ProjectFile projectFile = reader.read(filePath);     
        MSProject msProject = new MSProject(projectFile);
        // Import all resources
        Map<Resource, MSResource> msResources = this.importAllResources(projectFile, msProject);     
        // Import all tasks
        Map<Task, MSTask> msTasks = this.importAllTasks(projectFile, msProject);     
        // Import all resource assignments
       this.importAllResourceAssignments(projectFile, msProject, msResources, msTasks);
       // Return the result
       return msProject;  	
	}
    
    /**
     * {@inheritedDoc}
     */
    @Override
	public ProjectFile exportProject(MSProject msProject) {	
    	// Instantiates the result
    	ProjectFile projectFile = new ProjectFile();
    	// Export all resources
    	Map<MSResource, Resource> resources = this.exportAllResources(msProject, projectFile);	
    	// Export all tasks
    	Map<MSTask, Task> tasks = this.exportAllTasks(msProject, projectFile);  	
    	// Export all resource assignments
    	this.exportAllResourceAssignments(msProject, projectFile, resources, tasks); 	
    	// Fill the project header with the informations of the MSProject
    	this.fillProjectHeader(projectFile, msProject);
    	// Return the result
    	return projectFile;		
	}

    /**
     * {@inheritedDoc}
     */
    @Override
    public void exportProjectToMSPDIFile(MSProject msProject, InputStream input) throws IOException {
    	String destinationPath = IOUtils.toString(input);
    	ProjectFile projectFile = this.exportProject(msProject);	
    	MSPDIWriter writer = new MSPDIWriter();
		writer.write(projectFile, destinationPath /*+ msProject.getTitle() */+ "test.xml");   	
    }
    
    /**
     * Import all resources of a MS project file
     * @param projectFile an instance of ProjectFile (MPXJ API)
     * @param msProject an instance of MSProject
     * @return a Map which will be useful for the import of the resource assignments of the project
     */
    private Map<Resource, MSResource> importAllResources(ProjectFile projectFile, MSProject msProject) {	
		// Instantiates the result
		Map<Resource, MSResource> msResources = new HashMap<Resource, MSResource>();	
		// Get all resources
		List<Resource> resources = projectFile.getAllResources();
		// Check if the project has resources
		if (CollectionUtil.isNotEmpty(resources)) {
			for (Resource resource : resources) {
				// Check if the resource is not null and if it has a name
				if (resource != null && resource.getName() != null) {
					// Instantiates a new MSResource
	        		MSResource msResource = new MSResource(resource);
	        		// Binds the current MSResource to the current resource
	        		msResources.put(resource, msResource);     
				}
			}
			// All resources have been imported : we have now to add them to the project
			List<MSResource> msListResources = new ArrayList<MSResource>(msResources.values());
			msProject.setResources(msListResources);
		}
		// Return the result
		return msResources;		
	}
    
    /**
     * Import all tasks of a MS project file (recursive)
     * @param projectFile an instance of ProjectFile (MPXJ API)
     * @param msProject an instance of MSProject
     * @return a Map which will be useful for the import of the resource assignments of the project
     */
    private Map<Task, MSTask> importAllTasks(ProjectFile projectFile, MSProject msProject) {   	
    	// Instantiates the result
    	Map<Task, MSTask> msTasks = new HashMap<Task, MSTask>();   	
    	// Get child tasks of the project
        List<Task> projectChildTasks = projectFile.getChildTasks();       
        // Check if the project has child tasks
        if (CollectionUtil.isNotEmpty(projectChildTasks)) { 
        	// Instantiates the list of child tasks of the project
        	List<MSTask> msListProjectChildTasks = new ArrayList<MSTask>();
            for (Task childTask : projectChildTasks) {
            	// Check if the child task is not null, if it has a name and a duration
            	if (childTask != null && childTask.getName() != null && childTask.getDuration() != null) {
	            	// Instantiates a new MSTask
	            	MSTask msChildTask = new MSTask(childTask);
	            	// Binds the current MSTask to the current Task
	            	msTasks.put(childTask, msChildTask);
	            	// Import child tasks of the current child task
	            	this.importChildTasks(childTask, msChildTask, msTasks);
	            	// Add the child task to the list of project child tasks
	            	msListProjectChildTasks.add(msChildTask);
            	}
            }
            // Set the list of child tasks of the project
            msProject.setChildTasks(msListProjectChildTasks);
            // All the tasks have been imported : we have now to specify predecessors
	    	// Instantiates the list of relations of the project
	    	List<MSRelation> msListRelations = new ArrayList<MSRelation>();
            for(Entry<Task, MSTask> entry : msTasks.entrySet()) {
        		Task sourceTask = entry.getKey();
        	    MSTask msSourceTask = entry.getValue();      	    
        	    // Get relations of the current task
        	    List<Relation> relations = sourceTask.getPredecessors();
        	    // Check if the current task has predecessors
        	    if (CollectionUtil.isNotEmpty(relations)) {
        	    	for (Relation relation : relations) {
        	    		// Check if the relation is not null, if it has a source task, a target task and a lag
        	    		if (relation != null && relation.getSourceTask() != null && relation.getTargetTask() != null && relation.getLag() != null) {
	        	    		MSTask msTargetTask = msTasks.get(relation.getTargetTask());
	        	    		// Add the relation to the list of relations
	        	    		msListRelations.add(new MSRelation(msSourceTask, msTargetTask, relation));
        	    		}
        	    	}
        	    }
            }
	    	// Set the list of relations of the project
	    	msProject.setTaskPredecessors(msListRelations);
        }       
        // Return the result
        return msTasks;  	
    }
    
    /**
     * Import all of the hierarchy of tasks of the MS project file (recursive) - used in importAllTasks
     * @param parentTask the parentTask (Task - MPXJ API)
     * @param msParentTask the parentTask (MSTask)
     * @param msTasks a Map which will contain all the tasks of the project (Task and MSTask)
     */
    private void importChildTasks(Task parentTask, MSTask msParentTask, Map<Task, MSTask> msTasks) {  	
    	// Get child tasks of the parent task
    	List<Task> childTasks = parentTask.getChildTasks();   	
    	// Check if the parent task has child tasks
    	if (CollectionUtil.isNotEmpty(childTasks)) {
    		// Instantiates the list of child tasks of the parent task
    		List<MSTask> msListChildTasks = new ArrayList<MSTask>();
	    	for (Task childTask : childTasks) {
	    		// Check if the childTask is not null, if it has a name and a duration
	    		if (childTask != null && childTask.getName() != null && childTask.getDuration() != null) {
		    		// Instantiates a new child task (MSTask)
	            	MSTask msChildTask = new MSTask(childTask);   	
	            	// Binds the current MSTask to the current Task
	            	msTasks.put(childTask, msChildTask);           	
	            	// Import child tasks of the current child task
	            	this.importChildTasks(childTask, msChildTask, msTasks);
	            	// Add the current child task to the list of child task
	            	msListChildTasks.add(msChildTask);
	    		}
	    	}
	    	// Set the list of child tasks of the parent task
	    	msParentTask.setChildTasks(msListChildTasks);
    	}  	
    }
    
    /**
     * Import all resource assignments of a MS project file
     * @param projectFile an instance of ProjectFile (MPXJ API)
     * @param msProject an instance of MSProject
     * @param msResources a Map which contains all the resources of the project
     * @param msTasks a Map which contains all the resources of the project
     */
    private void importAllResourceAssignments(ProjectFile projectFile, MSProject msProject, 
    		Map<Resource, MSResource> msResources, Map<Task, MSTask> msTasks) {   	    
	    // Get resource assignments of the project
	    List<ResourceAssignment> resourceAssignments = projectFile.getAllResourceAssignments();    	    
	    // Check if the project has resource assignments
	    if (CollectionUtil.isNotEmpty(resourceAssignments)) {
	    	// Instantiates the list of resource assignments of the project
	    	List<MSResourceAssignment> msListResourceAssignments = new ArrayList<MSResourceAssignment>();
	    	for (ResourceAssignment resourceAssignment : resourceAssignments) {
	    		// Check if the resource assignment is not null, if it has a resource and a task
	    		if (resourceAssignment != null && resourceAssignment.getResource() != null && resourceAssignment.getTask() != null) {
		    		// Instantiates a new MSResourceAssignment
		    		MSResource msResource = msResources.get(resourceAssignment.getResource());
		    		MSTask msTask = msTasks.get(resourceAssignment.getTask());
					MSResourceAssignment msResourceAssignment = new MSResourceAssignment(resourceAssignment, msTask, msResource);
					// Add the resource assignment to list of resource assignments
					msListResourceAssignments.add(msResourceAssignment);
	    		}
			}
	    	// Set the list of resource assignments of the project
	    	msProject.setResourceAssignments(msListResourceAssignments);
	    }    
    }
    
    /**
     * Export all resources of a MSProject
     * @param msProject an instance of MSProject
     * @param projectFile an instance of MS project file (MPXJ API)
     * @return a Map which will be useful for the export of the resource assignments of the project
     * @throws MPXJException 
     */
    private Map<MSResource, Resource> exportAllResources(MSProject msProject, ProjectFile projectFile) {   	
    	// Instantiates the result
    	Map<MSResource, Resource> resources = new HashMap<MSResource, Resource>();	
    	// Get all MSResources
    	List<MSResource> msResources = msProject.getResources(); 	
    	// Check if there are MSResources
    	if (CollectionUtil.isNotEmpty(msResources)) { 		
    		for(MSResource msResource : msResources) {
    			// Add a new resource to the project
    			Resource resource = projectFile.addResource();
    			// Fill the resource with the informations of the MSResource
    			this.fillResource(resource, msResource);
    			// Binds the current Resource to the current MSResource
    			resources.put(msResource, resource);
    		}  		
    	}
    	// Return the result
    	return resources; 	
    }
    
    /**
     * Export all of the tasks of a MSProject (recursive)
     * @param msProject an instance of MSProject
     * @param projectFile an instance of MS project file (MPXJ API)
     * @return a Map which will be useful for the export of the resource assignments of the project
     */
    private Map<MSTask, Task> exportAllTasks(MSProject msProject, ProjectFile projectFile) {  	
    	// Instantiates the result
    	Map<MSTask, Task> tasks = new HashMap<MSTask, Task>();  	
    	// Get child tasks of the project
    	List<MSTask> msChildTasks = msProject.getChildTasks();  	
    	// Check if the project has child tasks
    	if (CollectionUtil.isNotEmpty(msChildTasks)) {
    		for (MSTask msChildTask : msChildTasks) {
    			// Add a new child task to the project
    			Task childTask = projectFile.addTask();
    			// Fill the child task with the informations of the MSTask
    			this.fillTask(childTask, msChildTask);
    			// Binds the current Task to the current MSTask
    			tasks.put(msChildTask, childTask);
    			// Export child tasks of the current child task
    			this.exportChildTasks(msChildTask, childTask, tasks);
    		}		
    		// All the tasks have been exported : we have now to specified predecessors
    		// Get the relations of the project
    		List<MSRelation> msRelations = msProject.getTaskPredecessors();
    		// Check if the project has relations
    		if (CollectionUtil.isNotEmpty(msRelations)) {
    			for (MSRelation msRelation : msRelations) {
    				Task sourceTask = tasks.get(msRelation.getSourceTask());
    	    		Task targetTask = tasks.get(msRelation.getTargetTask());
    	    		Integer relationType = msRelation.getRelationType();
    	    		MSDuration msLag = msRelation.getLag();
    	    		Duration lag = Duration.getInstance(msLag.getDuration().doubleValue(), 
    	    				TimeUnit.getInstance(msLag.getTimeUnit()));
    	    		sourceTask.addPredecessor(targetTask, RelationType.getInstance(relationType), lag);
    	    	}
    		}
    	}	
    	// Return the result
    	return tasks;   	
    }
    
    /**
     * Export all of the hierarchy of tasks of a MSProject (recursive) - used in exportAllTasks
     * @param msParentTask an instance of MSTask
     * @param parentTask an instance of Task (MPXJ API)
     * @param tasks a Map which will contain all the tasks of the project (MSTask and Task)
     */
    private void exportChildTasks(MSTask msParentTask, Task parentTask, Map<MSTask, Task> tasks) { 	
    	// Get child tasks of the parent task
    	List<MSTask> msChildTasks = msParentTask.getChildTasks(); 	
    	// Check if the parent task has child tasks
    	if (CollectionUtil.isNotEmpty(msChildTasks)) {
    		for (MSTask msChildTask : msChildTasks) {
    			// Add a new child task to the parent task
    			Task childTask = parentTask.addTask();
    			// Fill the child task with the informations of the MSTask
    			this.fillTask(childTask, msChildTask);
    			// Binds the current Task to the current MSTask
    			tasks.put(msChildTask, childTask);
    			// Export child tasks of the current child task
    			this.exportChildTasks(msChildTask, childTask, tasks);
    		}
    	}   	
    }
    
    /**
     * Export all of the resource assignments of a MSProject
     * @param resources a Map which contains all the resources of the project
     * @param tasks a Map which contains all the tasks of the project
     */
    private void exportAllResourceAssignments(MSProject msProject, ProjectFile projectFile, 
    		Map<MSResource, Resource> resources, Map<MSTask, Task> tasks) {
		// Get resource assignments of the project
		List<MSResourceAssignment> msResourceAssignments = msProject.getResourceAssignments();
		// Check if the project has resource assignments
		if (CollectionUtil.isNotEmpty(msResourceAssignments)) {
			 for (MSResourceAssignment msResourceAssignment : msResourceAssignments) {
				 // Instantiates a new ResourceAssignment
				 Resource resource = resources.get(msResourceAssignment.getResource());
				 Task task = tasks.get(msResourceAssignment.getTask());
				 ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
				 // Fill the resource assignment with the informations of the MSResourceAssignment
				 this.fillResourceAssignment(resourceAssignment, msResourceAssignment);
				 // Add the resource assignment to the resource
				 resource.addResourceAssignment(resourceAssignment);
			 }
		}
    }
    
    /**
     * Fill an instance of ProjectHeader (retrieved in an instance of ProjectFile) with the informations of an instance of MSProject
     * @param projectFile an instance of ProjectFile (MPXJ API)
     * @param msProject an instance of MSProject
     */
    private void fillProjectHeader(ProjectFile projectFile, MSProject msProject) {
    	ProjectHeader projectHeader = projectFile.getProjectHeader();
    	projectHeader.setProjectTitle(msProject.getTitle());
    	projectHeader.setKeywords(msProject.getKeywords());
    	projectHeader.setCompany(msProject.getCompany());
    	projectHeader.setCurrencySymbol(msProject.getCurrencySymbol());
    	projectHeader.setComments(msProject.getComments());
    }
    
    /**
     * Fill an instance of Resource with the informations of an instance of MSResource
     * @param resource an instance of Resource (MPXJ API)
     * @param msResource an instance of MSResource
     */
	private void fillResource(Resource resource, MSResource msResource) {
		resource.setName(msResource.getName());
		resource.setCost(msResource.getCost());
		resource.setType(ResourceType.getInstance(msResource.getType()));
		resource.setNotes(msResource.getNotes());
		//resource.setWork(Duration.getInstance(this.work.getDuration().doubleValue(),
		//		TimeUnit.getInstance(this.work.getTimeUnit())));
		//System.out.println(this.name + " w = " + resource.getWork() + " msW = " + Duration.getInstance(this.work.getDuration().doubleValue(), TimeUnit.getInstance(this.work.getTimeUnit())));
	}
    
	/**
	 * Fill an instance of Task with the informations of an instance of MSTask
	 * @param task an instance of Task
	 * @param msTask an instance of MSTask
	 */
	private void fillTask(Task task, MSTask msTask) {
		task.setName(msTask.getName());
		task.setStart(msTask.getStartDate());
		task.setFinish(msTask.getFinishDate());
		task.setDeadline(msTask.getDeadLine());		
		//task.setDuration(Duration.getInstance(this.duration.getDuration().doubleValue(), 
		//		TimeUnit.getInstance(this.duration.getTimeUnit())));
		//task.setWork(Duration.getInstance(this.work.getDuration().doubleValue(),
		//		TimeUnit.getInstance(this.work.getDuration().doubleValue())));
		task.setConstraintType(ConstraintType.getInstance(msTask.getConstraintType()));
		task.setConstraintDate(msTask.getConstraintDate());
		task.setPercentageComplete(msTask.getPercentageComplete());
		task.setPercentageWorkComplete(msTask.getPercentageWorkComplete());
		task.setPriority(Priority.getInstance(msTask.getPriority()));
		task.setNotes(msTask.getNotes());
	}
	
	/**
	 * Fill an instance of ResourceAssignment with the informations of an instance of MSResourceAssignment
	 * @param resourceAssignment an instance of ResourceAssignment
	 * @param msResourceAssignment an instance of MSResourceAssignment
	 */
	private void fillResourceAssignment(ResourceAssignment resourceAssignment, MSResourceAssignment msResourceAssignment) {
		resourceAssignment.setCost(msResourceAssignment.getCost());
		resourceAssignment.setActualCost(msResourceAssignment.getActualCost());
		resourceAssignment.setStart(msResourceAssignment.getStartDate());
		resourceAssignment.setFinish(msResourceAssignment.getFinishDate());
		resourceAssignment.setPercentageWorkComplete(msResourceAssignment.getPercentageWorkComplete());
		/*resourceAssignment.setWork(Duration.getInstance(this.work.getDuration().doubleValue(), 
				TimeUnit.getInstance(this.work.getTimeUnit())));
		resourceAssignment.setActualWork(Duration.getInstance(this.actualWork.getDuration().doubleValue(),
				TimeUnit.getInstance(this.actualWork.getTimeUnit())));*/
		resourceAssignment.setUnits(msResourceAssignment.getUnits());
		resourceAssignment.setNotes(msResourceAssignment.getNotes());
		
	}

}
