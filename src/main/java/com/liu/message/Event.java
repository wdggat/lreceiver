package com.liu.message;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class Event {
	public static final String EMAIL = "EMAIL";
	public static final String UID = "UID";
	public static final String USERNAME = "UNAME";
	public static final String PASSWORD = "PW";
	public static final String PASSWORD_NEW = "PW_NEW";
	public static final String BAIDU_USERID = "BD_UID";
	public static final String BAIDU_CHANNELID = "BD_CID";
	public Event() {}

	public Event(DataType dataType) {
		this.dataType = dataType;
		entrys = new HashMap<String, String>();
	}

	private DataType dataType;
	private Map<String, String> entrys;

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public Map<String, String> getEntrys() {
		return entrys;
	}

	public void setEntrys(Map<String, String> entrys) {
		this.entrys = entrys;
	}

	public void putEntry(String key, String value) {
		entrys.put(key, value);
	}
	
	public String getEntry(String key) {
		return entrys.get(key);
	}
	
	public String toJson() {
		return JSON.toJSONString(this);
	}
	
	@Override
	public String toString() {
		return toJson();
	}
}
