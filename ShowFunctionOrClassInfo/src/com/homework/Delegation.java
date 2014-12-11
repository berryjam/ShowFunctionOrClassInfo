package com.homework;

public class Delegation {
	public static final int FUNCTION_TYPE = 1;
	public static final int CLASS_TYPE = 2;

	public static void main(String[] args) {
		int type = Integer.parseInt(args[0]);
		switch (type) {
		case FUNCTION_TYPE:
			FunctionGUI.show();
			break;
		case CLASS_TYPE:
			ClassGUI.show();
			break;
		}
	}
}
