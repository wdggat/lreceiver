package com.liu.helper;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.liu.message.User;

@Ignore
public class UsersTest {
	@BeforeClass
	public static void setUp() {
		PropertyConfigurator.configure(Configuration.DEFAULT_CONF_PATH);
		
		String dbdriver = "com.mysql.jdbc.Driver";
		String dburl = "jdbc:mysql://localhost:3306/whoami_test?autoReconnect=true&amp;autoReconnectForPools=true";
		String username = "liu";
		String password = "pass_test";
		JDBCHelper.init(dbdriver, dburl, username, password);
	}
	
/*	@Test
	public void testInsertNewUser() {
		User u = new User();
		u.setBirthday(641606400l);
		u.setEmail("hzliuxiaolong@163.com");
		u.setGender(0);
		u.setPassword("wdggat");
		u.setPhone("15024405406");
		u.setProvince("杭州");
		u.setUid("hehe");
		long uid = Users.insertUserIntoDb(u);
		assertTrue(uid > 0);
	}*/
	
	@AfterClass
	public static void tearDown() {
		JDBCHelper.closeConnection();
	}
}
