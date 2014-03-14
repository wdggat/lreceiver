package com.liu.dispatcher;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import com.liu.helper.Configuration;
import com.liu.helper.QueueHelper;
import com.liu.message.AppMessage;
import com.liu.message.DataType;

public class QueueHelperTest {
	private static AppMessage makeAppMessage() {
		AppMessage msg = new AppMessage();
		msg.setAppChannel("qq");
		msg.setAppVersion("0.1");
		msg.setContent("内容_test");
		msg.setCostTime(300);
		msg.setDataType(DataType.MESSAGE.getValue());
		msg.setDeviceModel("小米");
		msg.setDeviceNetWork("wifi");
		msg.setDeviceOs("android");
		msg.setDeviceOsVersion("4.4");
		msg.setDeviceUdid("2fasf3qw3er23");
		msg.setMsgId("msgid_9527");
		msg.setMsgType("email");
		msg.setOccurTime(System.currentTimeMillis());
		msg.setSubject("邮件subject_test");
		List<String> to = new ArrayList<String>();
		to.add("wdggat@163.com");
		msg.setTo(to);
		msg.setUserId("userid_001");
		return msg;
	}
	
	public static void main(String args[]){
		PropertyConfigurator.configure(Configuration.DEFAULT_CONF_PATH);
		if(QueueHelper.init()){
			for(int i = 0; i < 1; i++){
			    AppMessage msg = makeAppMessage();
			    msg.setUserId("userid_" + i);
			    if(QueueHelper.enqueue(msg))
				    System.out.println("Enqueued: " + msg);
			}
//			QueueHelper.close();
		}
		
	}
}
