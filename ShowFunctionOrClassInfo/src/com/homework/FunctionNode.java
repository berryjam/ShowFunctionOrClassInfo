package com.homework;

public class FunctionNode {
	private String id;
	private String functionName;
	private String filePath;
	private int line;

	public FunctionNode(String id, String functionName, String filePath,
			int line) {
		this.id = id;
		this.functionName = functionName;
		this.filePath = filePath;
		this.line = line;
	}

	@Override
	public String toString() {
		return this.functionName;
	}

	public String getID() {
		return id;
	}

	public String getFunctionName() {
		return this.functionName;
	}

	public String getFilePath() {
		return filePath;
	}

	public int getLine() {
		return line;
	}

}
