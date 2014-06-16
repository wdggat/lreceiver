package com.liu.message;

import org.apache.commons.lang.StringUtils;
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
		if(StringUtils.isEmpty(loginE.getEntry(Event.USERNAME)) || StringUtils.isEmpty(loginE.getEntry(Event.PASSWORD))) 
			return false;
		return true;
	}

	public static boolean checkRegistEvent(Event registE) {
		String uinfo = registE.getEntry(Event.USER);
		if(!StringUtils.isEmpty(uinfo)) {
			User u = JSON.parseObject(uinfo, User.class);
			return u != null;
		}
		return false;
	}

	public static boolean checkPasswordForgetEvent(Event pfe) {
		if(StringUtils.isEmpty(pfe.getEntry(Event.USERNAME)))
			return false;
		return true;
	}

	public static boolean checkPasswordChangeEvent(Event pce) {
		if(StringUtils.isEmpty(pce.getEntry(Event.USERNAME)) || StringUtils.isEmpty(pce.getEntry(Event.PASSWORD)) || 
				StringUtils.isEmpty(pce.getEntry(Event.PASSWORD_NEW)))
			return false;
		return true;

	}

	public static boolean checkQuickMessage(Message msg) {
		//TOTO
	}

	public static boolean checkNewMsgMessage(Message msg) {

	}

	public static boolean checkReplyMsgMessage(Message msg) {
		//TODO
	}
}
