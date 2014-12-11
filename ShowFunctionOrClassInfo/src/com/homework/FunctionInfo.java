package com.homework;

import java.util.List;

public class FunctionInfo {

	private String id;
	private List<String> referencedbyID;
	private List<String> referenceID;

	public FunctionInfo(String id, List<String> referencedbyID,
			List<String> referenceID) {
		this.id = id;
		this.referencedbyID = referencedbyID;
		this.referenceID = referenceID;
	}

	public String getID() {
		return this.id;
	}

	public List<String> getReferencedbyID() {
		return this.referencedbyID;
	}

	public List<String> getReferenceID() {
		return this.referenceID;
	}
}
