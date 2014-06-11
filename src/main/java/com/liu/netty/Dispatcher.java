package com.liu.netty;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.liu.helper.Configuration;
import com.liu.helper.QueueHelper;
import com.liu.helper.RedisHelper;
import com.liu.message.DataType;
import com.liu.message.Event;
import com.liu.message.Message;
import com.liu.message.Request;
import com.liu.message.Response;
import com.liu.message.User;
import com.liu.message.Validator;

public class Dispatcher {
    private static Logger logger = Logger.getLogger(Dispatcher.class);

    public static Response dispatch(String inputJson) {
        if(inputJson == null || !Validator.checkInputJson(inputJson)) {
            logger.debug("Invalid json: " + inputJson);
            return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID,
                     "Invalid json");
        }
        
        Request req = JSON.parseObject(inputJson, Request.class);
        DataType dataType = req.getDataType();
        if(dataType.isTypeEvent())
        	return dispatchEvent(dataType, req.getJsonStr());
        if(dataType.isTypeMessage())
        	return dispatchMessage(dataType, req.getJsonStr());
        return new Response(417, "Can't get data type.");
    }
    
    private static Response dispatchMessage(DataType dataType, String inputContent) {
    	Message msg = JSON.parseObject(inputContent, Message.class);
        if (msg == null) {
            logger.debug("Invalid input parameters, can't parse to ShortMsgRequest, " + inputContent);
            return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID,
                     "Invalid input parameters");
        }
        
        try {
            logger.debug("MSG queue in ...");
            boolean enqueueResult = QueueHelper.enqueue(msg);
            if (enqueueResult) {
                logger.info("$Message enqueue: " + msg.toJson());
                return NettyResponse.genResponse(Configuration.RES_CODE_SUCC, "");
            } else {
                logger.info("$Message enqueue failed: " + msg.toJson());
                return NettyResponse.genResponse(Configuration.RES_CODE_SERVER_ERROR,
                         "Server error");
            }
        } catch (Throwable e) {
            logger.error("Message enqueue failed due to exception", e);
            return NettyResponse.genResponse(Configuration.RES_CODE_SERVER_ERROR,
                     "Server error");
        }
    }
    
    private static Response dispatchEvent(DataType dataType, String inputContent) {
    	Event event = JSON.parseObject(inputContent, Event.class);
        if (event == null) {
            logger.debug("Invalid input parameters, can't parse to ShortMsgRequest, " + inputContent);
            return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID,
                     "Invalid input parameters");
        }
        
        if(dataType.equals(DataType.LOGIN)) {
        	String userJson = RedisHelper.getUinfoCache(event.getEntry(Event.USERNAME));
        	if(!StringUtils.isEmpty(userJson)) {
        		User user = User.fromJsonStr(userJson);
        		if(user.getPassword().equals(event.getEntry(Event.PASSWORD)))
        			return NettyResponse.genResponse(Configuration.RES_CODE_SUCC, "");
        	}
        	return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID, "Username or password incorrect");
        } else if(dataType.equals(DataType.REGIST)) {
        	if(RedisHelper.existUinfoCache(event.getEntry(Event.USERNAME)) == RedisHelper.REDIS_KEY_NOT_EXISTS) {
        		User newUser = User.fromJsonStr(event.getEntry(Event.USER));
        	    RedisHelper.setUinfoCache(event.getEntry(Event.USERNAME), );
        	    
        	}
        } else if(dataType.equals(DataType.PASSWORD_CHANGE)) {
        	
        } else if(dataType.equals(DataType.PASSWORD_FORGET)) {
        	
        }
       /*        
       try {
            logger.debug("MSG queue in ...");
            boolean enqueueResult = QueueHelper.enqueue(event);
            if (enqueueResult) {
                logger.info("$Message enqueue: " + event.toJson());
                return NettyResponse.genResponse(Configuration.RES_CODE_SUCC, "");
            } else {
                logger.info("$Message enqueue failed: " + event.toJson());
                return NettyResponse.genResponse(Configuration.RES_CODE_SERVER_ERROR,
                         "Server error");
            }
        } catch (Throwable e) {
            logger.error("Message enqueue failed due to exception", e);
            return NettyResponse.genResponse(Configuration.RES_CODE_SERVER_ERROR,
                     "Server error");
        }*/
    }

/*    public static boolean sendMail(MailRequest mailRequest) {
        String hostName = Configuration.MAIL_HOST_NAME;

        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(hostName);

            email.setFrom(mailRequest.getFromAddress(), mailRequest.getFromName());
            email.addTo(mailRequest.getTo()[0] + "@" + Configuration.MAIL_DOMAIN);
            email.setSubject(mailRequest.getMailSubject());

            // set the html message
            email.setHtmlMsg(mailRequest.getMailContent());
            // set the alternative message
            email.setTextMsg("Your email client does not support HTML messages");

            email.send();
        } catch (Exception e) {
            logger.error("Error occurred during sending email", e);
            return false;
        }

        return true;
    }*/
}

