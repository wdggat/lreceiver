package com.liu.netty;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.liu.helper.Configuration;
import com.liu.helper.QueueHelper;
import com.liu.helper.RedisHelper;
import com.liu.helper.Users;
import com.liu.message.DataType;
import com.liu.message.Event;
import com.liu.message.MailSender;
import com.liu.message.Message;
import com.liu.message.Request;
import com.liu.message.Response;
import com.liu.message.User;
import com.liu.message.Validator;

public class Dispatcher {
    private static Logger logger = Logger.getLogger(Dispatcher.class);

    public static Response dispatch(String inputJson) {
        if(StringUtils.isEmpty(inputJson) || !Validator.checkInputJson(inputJson)) {
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
        if (!Validator.checkMessage(msg)) {
            logger.debug("Invalid input content, " + inputContent);
            return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID,
                     "Invalid input parameters");
        }
        
        normMessage(msg);
        
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
            logger.debug("Invalid input parameters, can't parse to Event, " + inputContent);
            return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID,
                     "Invalid input parameters");
        }
        
        if(dataType.equals(DataType.LOGIN)) {
        	if(!Validator.checkLoginEvent(event))
        		return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID, "Input invalid.");
        	String userJson = RedisHelper.getUinfoCache(event.getEntry(Event.USERNAME));
        	if(!StringUtils.isEmpty(userJson)) {
        		User user = User.fromJsonStr(userJson);
        		if(user.getPassword().equals(event.getEntry(Event.PASSWORD)))
        			return NettyResponse.genResponse(Configuration.RES_CODE_SUCC, userJson);
        	}
        	return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID, "Username or password incorrect");
        } else if(dataType.equals(DataType.REGIST)) {
			if (RedisHelper.existUinfoCache(event.getEntry(Event.USERNAME)) == RedisHelper.REDIS_KEY_NOT_EXISTS) {
				User newUser = User.fromJsonStr(event.getEntry(Event.USER));
//				logger.info("uid assigned for username, username: " + newUser.getEmail() + ", userid: " + userid);
				if (Users.addUser(newUser)) {
					return NettyResponse.genResponse(Configuration.RES_CODE_SUCC, newUser.toJson());
				}
				return NettyResponse.genResponse(Configuration.RES_CODE_SERVER_ERROR, "Sorry, 内部错误.");
			}
			return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID, "Sorry, 此邮箱已被注册.");
        } else if(dataType.equals(DataType.PASSWORD_CHANGE)) {
        	String username = event.getEntry(Event.USERNAME);
        	if (RedisHelper.existUinfoCache(username) == RedisHelper.REDIS_KEY_EXISTS) {
        		User user = User.fromJsonStr(RedisHelper.getUinfoCache(username));
        		if (event.getEntry(Event.PASSWORD).equals(user.getPassword())) {
        			user.setPassword(event.getEntry(Event.PASSWORD_NEW));
        		    RedisHelper.setUinfoCache(username, user.toJson());
        		    if(Users.updateUserInfo(user) && RedisHelper.setUinfoCache(user.getEmail(), user.toJson())) {
        	            logger.info("user info updated, " + user.toJson());
        	            return NettyResponse.genResponse(Configuration.RES_CODE_SUCC, "");
        		    }
        		    return NettyResponse.genResponse(Configuration.RES_CODE_SERVER_ERROR, "Sorry, 再试一次?");
        		}
        		return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID, "抱歉,旧密码不正确.");
        	}
        	return NettyResponse.genResponse(Configuration.RES_CODE_SERVER_ERROR, "Sorry, 再试一次?");
        } else if(dataType.equals(DataType.PASSWORD_FORGET)) {
        	String to = event.getEntry(Event.USERNAME);
        	User user = User.fromJsonStr(RedisHelper.getUinfoCache(to));
        	MailSender.sendSimpleMail(to, "密码通知", "Your password: " + user.getPassword());
        	return NettyResponse.genResponse(Configuration.RES_CODE_SUCC, "");
        } else if(dataType.equals(DataType.BAIDU_PUSH_BIND)) {
        	String username = event.getEntry(Event.USERNAME);
        	String uid = event.getEntry(Event.USER);
        	String baiduUserId = event.getEntry(Event.BAIDU_USERID);
        	String baiduChannelId = event.getEntry(Event.BAIDU_CHANNELID);
        	List<String> baiduUinfo = new ArrayList<String>();
        	baiduUinfo.add(baiduUserId);
        	baiduUinfo.add(baiduChannelId);
        	if(RedisHelper.setBaiduUserCacheUname(username, baiduUinfo) && RedisHelper.setBaiduUserCacheUid(uid, baiduUinfo))
        		return NettyResponse.genResponse(Configuration.RES_CODE_SUCC, "");
        	return NettyResponse.genResponse(Configuration.RES_CODE_SERVER_ERROR, "Sorry, 再试一次?");
        }
        return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID, "数据类型错误.");
    }
    
    private static void normMessage(Message msg) {
    	msg.setContent(msg.getContent().trim());
    	msg.setFrom(msg.getFrom().trim());
    	msg.setFromUid(msg.getFromUid().trim());
    	msg.setSubject(msg.getSubject().trim());
    	msg.setTo(msg.getTo().trim());
    }
}

