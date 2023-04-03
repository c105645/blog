package com.ness.postservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag extends Base{

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
		return "Tag [name=" + name + ", description=" + description + ", id=" + id + ", createdAt=" + createdAt
				+ ",createdBy=" + createdBy + ", updatedAt=" + updatedAt + "]";
	}

	
}
