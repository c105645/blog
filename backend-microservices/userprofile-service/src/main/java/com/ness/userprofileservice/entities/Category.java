package com.ness.userprofileservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="categories")
public class Category extends Base {

	
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Category [name=" + name + ", description=" + description + ", id=" + id + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
	
	

}
