package com.liu.message;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import com.liu.helper.Configuration;

public class MailSender {
	private static final Logger logger = Logger.getLogger(MailSender.class);
	private static Configuration conf = new Configuration();
	
	public static boolean sendSimpleMail(String to, String subject, String content){
		try {
			Email email = new SimpleEmail();
			email.setHostName(conf.getMailHost163());
			email.setSmtpPort(conf.getMail163Port());
			email.setAuthenticator(new DefaultAuthenticator(conf.getMail163FromAddr(), conf.getMail163FromPassword()));
			email.setSSLOnConnect(true);
			email.setFrom(conf.getMail163FromAddr(), conf.getMailFromName());
			email.setSubject(subject);
			email.setMsg(content);
            email.addTo(to);
			email.send();
			return true;
		} catch (EmailException e) {
			logger.error("exception when sending simple mail, ", e);
			return false;
		}
	}

}
