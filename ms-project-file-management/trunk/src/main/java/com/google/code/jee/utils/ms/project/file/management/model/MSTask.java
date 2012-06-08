package com.google.code.jee.utils.ms.project.file.management.model;

import java.util.Date;
import java.util.List;

import net.sf.mpxj.Task;

public class MSTask {
		
	private String name;
	
	private Date startDate;
	
	private Date finishDate;
	
	private Date deadLine;
	
	private MSDuration duration;
	
	private MSDuration work;
	
	private Integer constraintType;
	
	private Date constraintDate;
	
	private Number percentageComplete;
	
	private Number percentageWorkComplete;
	
	private Integer priority;
	
	private String notes;
	
	private List<MSTask> childTasks;

	public MSTask() {}
	
	public MSTask(Task task) {
		this.name = task.getName();
		this.startDate = task.getStart();
		this.finishDate = task.getFinish();
		this.deadLine = task.getDeadline();
    	//this.duration = new MSDuration(task.getDuration());
    	this.work = new MSDuration(task.getWork());
		this.constraintType = task.getConstraintType().getValue();
		this.constraintDate = task.getConstraintDate();
		this.percentageComplete = task.getPercentageComplete();
		this.percentageWorkComplete = task.getPercentageWorkComplete();
		this.priority = task.getPriority().getValue();
		this.notes = task.getNotes();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the deadLine
	 */
	public Date getDeadLine() {
		return deadLine;
	}

	/**
	 * @param deadLine the deadLine to set
	 */
	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	/**
	 * @return the duration
	 */
	public MSDuration getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(MSDuration duration) {
		this.duration = duration;
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
	 * @return the constraintType
	 */
	public Integer getConstraintType() {
		return constraintType;
	}

	/**
	 * @param constraintType the constraintType to set
	 */
	public void setConstraintType(Integer constraintType) {
		this.constraintType = constraintType;
	}

	/**
	 * @return the constraintDate
	 */
	public Date getConstraintDate() {
		return constraintDate;
	}

	/**
	 * @param constraintDate the constraintDate to set
	 */
	public void setConstraintDate(Date constraintDate) {
		this.constraintDate = constraintDate;
	}

	/**
	 * @return the percentageComplete
	 */
	public Number getPercentageComplete() {
		return percentageComplete;
	}

	/**
	 * @param percentageComplete the percentageComplete to set
	 */
	public void setPercentageComplete(Number percentageComplete) {
		this.percentageComplete = percentageComplete;
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
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
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

}
