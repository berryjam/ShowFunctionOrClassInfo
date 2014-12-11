package com.homework;

import java.util.List;

public class ClassInfo {
	private String id;
	private List<String> relatedClassID;

	public ClassInfo(String id, List<String> relatedClassID) {
		this.id = id;
		this.relatedClassID = relatedClassID;
	}

	public String getID() {
		return this.id;
	}

	public List<String> getRelatedClassID() {
		return this.relatedClassID;
	}
}
