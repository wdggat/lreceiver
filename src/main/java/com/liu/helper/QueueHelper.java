package com.liu.helper;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQSession;
import org.apache.log4j.Logger;

import com.liu.message.Message;

public class QueueHelper {
	private static final Logger logger = Logger.getLogger(QueueHelper.class);

	private static final Configuration conf = new Configuration();
	private static ActiveMQConnection conn = null;
	private static ActiveMQSession session = null;
	private static ActiveMQMessageProducer producer = null;

	public static boolean init() {
		ActiveMQConnectionFactory connectFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, conf.getMQBrokerUrl());
		try {
			conn = (ActiveMQConnection) connectFactory.createConnection();
			conn.setAlwaysSessionAsync(false);
			conn.start();
			session = (ActiveMQSession) conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(conf.getMQQueueName());
			producer = (ActiveMQMessageProducer) session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		} catch (Exception e) {
			logger.error("MQ init failed,", e);
			if(conn != null && !conn.isClosed())
				close();
			return false;
		}
		return true;
	}
	
	public static void close() {
		try {
			producer.close();
//			session.commit();
			conn.close();
		} catch (Exception e) {
			logger.error("MQ failed to close, ", e);
		}
	}

	public static boolean enqueue(Message msg) {
		try {
			TextMessage textMessage = session.createTextMessage(msg.toJson());
			producer.send(textMessage);
//			session.commit();
		} catch (Exception e) {
			logger.error("enqueue failed,", e);
			return false;
		}
		return true;
	}
}
