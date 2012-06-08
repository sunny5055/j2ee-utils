package com.google.code.jee.utils.ms.project.file.management.model;

import java.util.Date;

import net.sf.mpxj.ResourceAssignment;

public class MSResourceAssignment {
	
	private MSTask task;
	
	private MSResource resource;
	
	private Number cost;
	
	private Number actualCost;
	
	private Date startDate;
	
	private Date finishDate;
	
	private Number percentageWorkComplete;
	
	private MSDuration work;
	
	private MSDuration actualWork;
	
	private Number units;
	
	private String notes;
	
	public MSResourceAssignment() {}
	
	public MSResourceAssignment(ResourceAssignment resourceAssignment, MSTask task, MSResource resource) {
		this.task = task;
		this.resource = resource;
		this.cost = resourceAssignment.getCost();
		this.actualCost = resourceAssignment.getActualCost();
		this.startDate = resourceAssignment.getStart();
		this.finishDate = resourceAssignment.getFinish();
		this.percentageWorkComplete = resourceAssignment.getPercentageWorkComplete();
		this.work = new MSDuration(resourceAssignment.getWork());
		this.actualWork = new MSDuration(resourceAssignment.getActualWork());
		this.units = resourceAssignment.getUnits();
		this.notes = resourceAssignment.getNotes();
	}

	/**
	 * @return the task
	 */
	public MSTask getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(MSTask task) {
		this.task = task;
	}
	
	/**
	 * @return the resource
	 */
	public MSResource getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(MSResource resource) {
		this.resource = resource;
	}

	/**
	 * @return the cost
	 */
	public Number getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(Number cost) {
		this.cost = cost;
	}

	/**
	 * @return the actualCost
	 */
	public Number getActualCost() {
		return actualCost;
	}

	/**
	 * @param actualCost the actualCost to set
	 */
	public void setActualCost(Number actualCost) {
		this.actualCost = actualCost;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the finishDate
	 */
	public Date getFinishDate() {
		return finishDate;
	}

	/**
	 * @param finishDate the finishDate to set
	 */
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	/**
	 * @return the percentageWorkComplete
	 */
	public Number getPercentageWorkComplete() {
		return percentageWorkComplete;
	}

	/**
	 * @param percentageWorkComplete the percentageWorkComplete to set
	 */
	public void setPercentageWorkComplete(Number percentageWorkComplete) {
		this.percentageWorkComplete = percentageWorkComplete;
	}

	/**
	 * @return the work
	 */
	public MSDuration getWork() {
		return work;
	}

	/**
	 * @param work the work to set
	 */
	public void setWork(MSDuration work) {
		this.work = work;
	}

	/**
	 * @return the actualWork
	 */
	public MSDuration getActualWork() {
		return actualWork;
	}

	/**
	 * @param actualWork the actualWork to set
	 */
	public void setActualWork(MSDuration actualWork) {
		this.actualWork = actualWork;
	}

	/**
	 * @return the units
	 */
	public Number getUnits() {
		return units;
	}

	/**
	 * @param units the units to set
	 */
	public void setUnits(Number units) {
		this.units = units;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
