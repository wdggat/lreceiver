package com.liu.message;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class EmailMsg{
	private String from;
	private List<String> to;
	private String subject;
	private String content;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void send() {
		//TODO
	}

	public static EmailMsg getFromJson(String jsonStr) {
		return JSON.parseObject(jsonStr, EmailMsg.class);
	}
	
	public String toJsonStr() {
		return JSON.toJSONString(this);
	}

}
