package com.liu.message;

import com.alibaba.fastjson.JSON;
import com.liu.message.DataType;

public class Request {
	private DataType dataType;
	private String jsonStr;
	public Request(DataType dataType, String jsonStr) {
		this.dataType = dataType;
		this.jsonStr = jsonStr;
	}
	
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String toJson() {
		return JSON.toJSONString(this);
	}
}
