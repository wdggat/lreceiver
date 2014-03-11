package com.liu.message;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class Validator {
    public static Logger logger = Logger.getLogger(Validator.class);
    
    public static boolean checkInputJson(String inputJson) {
        try {
            AppMessage appMsg = JSON.parseObject(inputJson, AppMessage.class);
            return appMsg != null;
        } catch (Exception e) {
            logger.info("Exception occurs in checkInputJson: " + inputJson, e);
            return false;
        }
    }
}
