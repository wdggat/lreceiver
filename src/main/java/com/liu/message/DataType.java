package com.liu.message;

public enum DataType {
	NONE(-1), REGIST(1), LOGIN(2), NEW_MSG(3), REPLY(4), QUICK_MSG(5), PASSWORD_FORGET(6), PASSWORD_CHANGE(7);
	private int code;

	private DataType(int code) {
		this.code = code;
	}
	
	public int getValue() {
		return code;
	}

	public static DataType getByValue(int code) {
		switch (code) {
		case 1:
			return REGIST;
		case 2:
			return LOGIN;
		case 3:
			return NEW_MSG;
		case 4:
			return REPLY;
		case 5:
			return QUICK_MSG;
		case 6:
			return PASSWORD_FORGET;
		case 7:
			return PASSWORD_CHANGE;
		default:
			return NONE;
		}
	}
}
