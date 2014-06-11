package com.liu.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.liu.message.User;

public class Users {
	private static final Logger logger = Logger.getLogger(Users.class);
	
	public static boolean init() {
		try {
			List<User> users = readAllUsers();
			for(User user : users) {
				RedisHelper.setUinfoCache(user.getEmail(), user.toJson());
			}
			logger.info(users.size() + " users info cached.");
			return true;
		} catch (Exception e) {
			logger.error("Exception when initing Users", e);
			return false;
		}
	}
	
	private static List<User> readAllUsers() throws Exception {
		List<User> users = new ArrayList<User>();
		String sql = "select uid,email,password,gender,province,phone,birthday from user;";
		Connection conn = JDBCHelper.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String uid = rs.getString(0);
			String email = rs.getString(1);
			String password = rs.getString(2);
			int gender = rs.getInt(3);
			String province = rs.getString(4);
			String phone = rs.getString(5);
			long birthday = rs.getLong(6);
			User user = new User(email, gender, province, birthday, phone, password, uid);
			users.add(user);
		}
		return users;
	}
	
	public static boolean addUser(User user) {
		
	}
	
	public static String generateUid(String username) {
		return UUID.randomUUID().toString();
	}
	
}
