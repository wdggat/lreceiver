package com.liu.netty;

import org.apache.log4j.PropertyConfigurator;

import com.liu.helper.Configuration;
import com.liu.helper.JDBCHelper;
import com.liu.helper.RedisHelper;
import com.liu.message.Response;

public class DispatcherTest {
	public static void main(String[] argv) {
		PropertyConfigurator.configure(Configuration.DEFAULT_CONF_PATH);
		Configuration conf = new Configuration();
		RedisHelper.init(conf.getRedisServerMaster(), conf.getRedisServerSlave());
		JDBCHelper.init(conf.getDbDriver(), conf.getMysqlUrl(), conf.getMysqlUsername(), conf.getMysqlPassword());
		
		String testInput = "{\"dataType\":\"REGIST\",\"jsonStr\":\"{\\\"dataType\\\":\\\"REGIST\\\",\\\"entrys\\\":{\\\"USER\\\":\\\"{\\\\\\\"birthday\\\\\\\":641653640,\\\\\\\"email\\\\\\\":\\\\\\\"test@163.com\\\\\\\",\\\\\\\"gender\\\\\\\":0,\\\\\\\"password\\\\\\\":\\\\\\\"wdggat094807\\\\\\\",\\\\\\\"phone\\\\\\\":\\\\\\\"15024405406\\\\\\\",\\\\\\\"province\\\\\\\":\\\\\\\"杭州\\\\\\\",\\\\\\\"uid\\\\\\\":\\\\\\\"\\\\\\\"}\\\"}}\"}";
		Response res = Dispatcher.dispatch(testInput);
		System.out.println(res.toString());
	}
}
