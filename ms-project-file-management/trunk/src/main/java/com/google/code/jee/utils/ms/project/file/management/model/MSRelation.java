package com.google.code.jee.utils.ms.project.file.management.model;

import net.sf.mpxj.Relation;

public class MSRelation {
	
	private MSTask sourceTask;
	
	private MSTask targetTask;
	
	private Integer relationType;
	
	private MSDuration lag;
	
	public MSRelation() {}
	
	public MSRelation(MSTask sourceTask, MSTask targetTask, Relation relation) {
		this.sourceTask = sourceTask;
		this.targetTask = targetTask;
		this.relationType = relation.getType().getValue();
		this.lag = new MSDuration(relation.getLag());
	}

	/**
	 * @return the sourceTask
	 */
	public MSTask getSourceTask() {
		return sourceTask;
	}

	/**
	 * @param sourceTask the sourceTask to set
	 */
	public void setSourceTask(MSTask sourceTask) {
		this.sourceTask = sourceTask;
	}

	/**
	 * @return the targetTask
	 */
	public MSTask getTargetTask() {
		return targetTask;
	}

	/**
	 * @param targetTask the targetTask to set
	 */
	public void setTargetTask(MSTask targetTask) {
		this.targetTask = targetTask;
	}

	/**
	 * @return the relationType
	 */
	public Integer getRelationType() {
		return relationType;
	}

	/**
	 * @param relationType the relationType to set
	 */
	public void setRelationType(Integer relationType) {
		this.relationType = relationType;
	}

	/**
	 * @return the lag
	 */
	public MSDuration getLag() {
		return lag;
	}

	/**
	 * @param lag the lag to set
	 */
	public void setLag(MSDuration lag) {
		this.lag = lag;
	}

}
