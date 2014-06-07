package com.liu.message;

import com.alibaba.fastjson.JSON;

public class Response {
	private static final int SUCCESS_CODE = 200;
	private static final int NETWORD_UNREACHABLE = -1;
	public static final Response DEMO_SUCCESS = new Response(SUCCESS_CODE, "");
	
	private int code;
	private String content;
	
	public Response() {}
	public Response(int code, String content) {
		super();
		this.code = code;
		this.content = content;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	public static Response fromRequestReturn(String ret) {
		if(ret == null)
			return new Response(-1, "");
		return JSON.parseObject(ret, Response.class);
	}
	
	public boolean succeed() {
		return code == SUCCESS_CODE;
	}
	
	public boolean networkUnreachable() {
		return code == NETWORD_UNREACHABLE;
	}
	
}
