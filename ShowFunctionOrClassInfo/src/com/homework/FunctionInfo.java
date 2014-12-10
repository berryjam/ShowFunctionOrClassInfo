package com.homework;

import java.util.List;

public class FunctionInfo {
	private String name;
	private int referencedbyCount;
	private int referenceCount;

	private String id;
	private List<String> referencedbyID;
	private List<String> referenceID;

	public FunctionInfo(String name, int referenceCount, int referencedbyCount) {
		this.name = name;
		this.referencedbyCount = referencedbyCount;
		this.referenceCount = referenceCount;
	}

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

	public String getName() {
		return this.name;
	}

	public int getReferencedbyCount() {
		return this.referencedbyCount;
	}

	public int getReferenceCount() {
		return this.referenceCount;
	}
}
