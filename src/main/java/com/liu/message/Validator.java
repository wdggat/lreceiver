package com.liu.message;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class Validator {
	public static Logger logger = Logger.getLogger(Validator.class);

	public static boolean checkInputJson(String inputJson) {
		try {
			Request req = JSON.parseObject(inputJson, Request.class);
			return req != null;
		} catch (Exception e) {
			logger.info("Exception occurs in checkInputJson: " + inputJson, e);
			return false;
		}
	}

	public static boolean checkLoginEvent(Event loginE) {

	}

	public static boolean checkRegistEvent(Event registE) {

	}

	public static boolean checkPasswordForgetEvent(Event pfe) {

	}

	public static boolean checkPasswordChangeEvent(Event pce) {

	}

	public static boolean checkQuickMessage(Message msg) {

	}

	public static boolean checkNewMsgMessage(Message msg) {

	}

	public static boolean checkReplyMsgMessage(Message msg) {
		//TODO
	}
}
