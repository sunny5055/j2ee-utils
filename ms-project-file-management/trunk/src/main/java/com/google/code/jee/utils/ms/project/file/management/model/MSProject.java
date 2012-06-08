package com.google.code.jee.utils.ms.project.file.management.model;

import java.util.List;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectHeader;

/**
 * The Class MSProject.
 */
public class MSProject {
	
	private String title;
	
	private String keywords;
	
	private String company;
	
	private String currencySymbol;
	
	private String comments;
	
	private List<MSResource> resources;
	
	private List<MSTask> childTasks;
	
	private List<MSRelation> taskPredecessors;
	
	private List<MSResourceAssignment> resourceAssignments;

	public MSProject() {}
	
	public MSProject(ProjectFile projectFile) {
		// Get the project header which contains the MS Project's main informations
        ProjectHeader projectHeader = projectFile.getProjectHeader();      
        // Set project's informations
        this.title = projectHeader.getProjectTitle();
        this.keywords = projectHeader.getKeywords();
        this.company = projectHeader.getCompany();
        this.currencySymbol = projectHeader.getCurrencySymbol();
        this.comments = projectHeader.getComments();
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the currencySymbol
	 */
	public String getCurrencySymbol() {
		return currencySymbol;
	}

	/**
	 * @param currencySymbol the currencySymbol to set
	 */
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	

	/**
	 * @return the resources
	 */
	public List<MSResource> getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(List<MSResource> resources) {
		this.resources = resources;
	}

	/**
	 * @return the childTasks
	 */
	public List<MSTask> getChildTasks() {
		return childTasks;
	}

	/**
	 * @param childTasks the childTasks to set
	 */
	public void setChildTasks(List<MSTask> childTasks) {
		this.childTasks = childTasks;
	}

	/**
	 * @return the taskPredecessors
	 */
	public List<MSRelation> getTaskPredecessors() {
		return taskPredecessors;
	}

	/**
	 * @param taskPredecessors the taskPredecessors to set
	 */
	public void setTaskPredecessors(List<MSRelation> taskPredecessors) {
		this.taskPredecessors = taskPredecessors;
	}

	/**
	 * @return the resourceAssignments
	 */
	public List<MSResourceAssignment> getResourceAssignments() {
		return resourceAssignments;
	}

	/**
	 * @param resourceAssignments the resourceAssignments to set
	 */
	public void setResourceAssignments(List<MSResourceAssignment> resourceAssignments) {
		this.resourceAssignments = resourceAssignments;
	}
	
}
