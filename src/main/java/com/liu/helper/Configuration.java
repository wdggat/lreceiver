package com.liu.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {
    private static Logger logger = Logger.getLogger(Configuration.class);

    private Properties properties;

    public final static String DEFAULT_CONF_PATH
            = System.getProperty("user.dir") + "/conf/conf.properties";
    public final static String REQUEST_LOG_FOLDER = "logs/";

    public final static String RES_CODE = "code";
    public final static String RES_MSGID = "msgId";
    public final static String RES_ERRMSG = "errMsg";
    public final static int RES_CODE_SUCC = 200;
    public final static int RES_CODE_INPUT_INVALID = 417;
    public final static int RES_CODE_SERVER_ERROR = 500;

    private InputStream getDefaultConfInputStream() {
        try {
            return new FileInputStream(DEFAULT_CONF_PATH);
        } catch (FileNotFoundException e) {
            logger.error("No configuration file found!", e);
            System.exit(1);
            return null;
        }
    }

    public Configuration() {
        loadConf(getDefaultConfInputStream());
    }

    protected void loadConf(InputStream in) {
        properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            logger.error("Failed to load configuration", e);
        }
    }

    public void reloadConf() {
        loadConf(getDefaultConfInputStream());
    }
    
    public String getMQBrokerUrl() {
    	return properties.getProperty("mq_broker");
    }
    
    public String getMQQueueName() {
    	return properties.getProperty("mq_queuename");
    }
    
    public String getCryptKey() {
    	return properties.getProperty("crypt_key");
    }
}
