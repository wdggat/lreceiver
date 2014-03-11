package com.liu.helper;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.liu.message.AppMessage;

public class QueueHelper {
	private static final Logger logger = Logger.getLogger(QueueHelper.class);

	private static final Configuration conf = new Configuration();
	private static Connection conn = null;
	private static Session session = null;
	private static MessageProducer producer = null;

	public static boolean init() {
		ConnectionFactory connectFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, conf.getMQBrokerUrl());
		try {
			conn = connectFactory.createConnection();
			conn.start();
			session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("email.online");
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		} catch (Exception e) {
			logger.error("MQ init failed,", e);
			if(conn != null)
				try {
					conn.close();
				} catch (JMSException e2) {
					logger.error("MQ close failed,", e2);
				}
			return false;
		}
		return true;
	}

	public static boolean enqueue(AppMessage msg) {
		try {
			TextMessage textMessage = session.createTextMessage(msg.toJsonStr());
			producer.send(textMessage);
		} catch (Exception e) {
			logger.error("enqueue failed,", e);
			return false;
		}
		return true;
	}
}
