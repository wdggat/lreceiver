package com.liu.netty;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.liu.helper.Configuration;
import com.liu.helper.QueueHelper;
import com.liu.message.Message;
import com.liu.message.Response;
import com.liu.message.Validator;

public class Dispatcher {
    private static Logger logger = Logger.getLogger(Dispatcher.class);

    public static Response dispatchMsg(String inputJson) {
        if(inputJson == null || !Validator.checkInputJson(inputJson)) {
            logger.debug("Invalid json: " + inputJson);
            return NettyResponse.genResponse(Configuration.RES_CODE_INPUT_INVALID,
                     "Invalid json");
        }
        
        Message msg = JSON.parseObject(inputJson, Message.class);
        if (msg == null) {
            logger.debug("Invalid input parameters, can't parse to ShortMsgRequest, " + inputJson);
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

