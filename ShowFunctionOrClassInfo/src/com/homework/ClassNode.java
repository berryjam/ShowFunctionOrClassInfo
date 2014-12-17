package com.homework;

public class ClassNode {
	private String id;
	private String className;
	private String filePath;

	public ClassNode(String id, String className, String filePath) {
		this.id = id;
		this.className = className;
		this.filePath = filePath;
	}

	public String getId() {
		return id;
	}

	public String getClassName() {
		return className;
	}

	public String getFilePath() {
		return filePath;
	}

	@Override
	public String toString() {
		return this.className;
	}
}
