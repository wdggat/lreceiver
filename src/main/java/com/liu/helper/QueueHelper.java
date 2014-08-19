package com.liu.helper;

import org.apache.log4j.Logger;

import com.liu.message.Message;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class QueueHelper {
	private static final Logger logger = Logger.getLogger(QueueHelper.class);

	private static final Configuration conf = new Configuration();
	private static Connection connection = null;
	private static Channel channel;
	
	public static boolean init() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(conf.getMQHost());
			factory.setPort(conf.getMQPort());
			factory.setUsername(conf.getMQUser());
			factory.setPassword(conf.getMQPassword());
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(conf.getMQQueueName(), true, false, false,	null);
			return true;
		} catch (Exception e) {
			logger.error("MQ init failed", e);
			if (connection != null && connection.isOpen())
				close();
			return false;
		}
	}
	
	public static void close() {
		try {
			 channel.close();
			 connection.close();
		} catch (Exception e) {
			logger.error("MQ failed to close, ", e);
		}
	}

	public static boolean enqueue(Message msg) {
		try {
			channel.basicPublish("", conf.getMQQueueName(), null, msg.toJson().getBytes());
		} catch (Exception e) {
			logger.error("enqueue failed,", e);
			return false;
		}
		return true;
	}
}
