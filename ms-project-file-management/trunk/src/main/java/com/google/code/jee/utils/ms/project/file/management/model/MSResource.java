package com.google.code.jee.utils.ms.project.file.management.model;

import net.sf.mpxj.Resource;

public class MSResource {
	
	private String name;
	
	private Number cost;
	
	private Integer type;
	
	private String notes;
		
	public MSResource() {}
	
	public MSResource(Resource resource) {
		this.name = resource.getName();
		this.cost = resource.getCost();
		this.type = resource.getType().getValue();
		this.notes = resource.getNotes();
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
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
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
