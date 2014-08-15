package com.liu.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.liu.message.User;

public class Users {
	private static final Logger logger = Logger.getLogger(Users.class);

	// no need
	/*public static boolean init() {
		try {
			List<User> users = readAllUsers();
			for (User user : users) {
				RedisHelper.setUinfoCache(user.getEmail(), user.toJson());
			}
			logger.info(users.size() + " users info cached.");
			return true;
		} catch (Exception e) {
			logger.error("Exception when initing Users", e);
			return false;
		}
	}*/

	private static List<User> readAllUsers() throws Exception {
		List<User> users = new ArrayList<User>();
		String sql = "select uid,email,password,gender,province,phone,birthday from user;";
		Connection conn = JDBCHelper.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String uid = rs.getString(1);
			String email = rs.getString(2);
			String password = rs.getString(3);
			int gender = rs.getInt(4);
			String province = rs.getString(5);
			String phone = rs.getString(6);
			long birthday = rs.getLong(7);
			User user = new User(email, gender, province, birthday, phone,
					password, uid);
			users.add(user);
		}
		return users;
	}

	// db then redis
	/*public static boolean addUser(User user) {
		long uid = insertUserIntoDb(user);
		if(uid == 0 || uid == -1) {
			return false;
		} else {
			user.setUid(uid + "");
			if (RedisHelper.setUinfoCache(user.getEmail(), user.toJson())) {
				logger.info("New user added, " + user.toJson());
				return true;
			}
			logger.info("New user added into db, but not cached, " + user.toJson());
			return true;
		}
	}*/
	
	public static boolean addUser(User user) {
		long newUid = RedisHelper.incrUid();
		if(newUid == RedisHelper.REDIS_SERVER_ERROR)
			return false;
		user.setUid(newUid + "");
		if (RedisHelper.setUinfoCache(user.getEmail(), user.toJson())) {
			insertUserIntoDb(user);
			logger.info("New user added, " + user.toJson());
			return true;
		}
		return false;
	}
	
	public static boolean updateUserInfo(User user) {
		Connection conn;
		PreparedStatement ps = null;
		try {
			conn = JDBCHelper.getConnection();
			ps = conn.prepareStatement("UPDATE user SET password=?,gender=?,province=?,phone=?,birthday=? where email=?;");
			ps.setString(1, user.getPassword());
			ps.setInt(2, user.getGender());
			ps.setString(3, user.getProvince());
			ps.setString(4, user.getPhone());
			ps.setLong(5, user.getBirthday());
			ps.setString(6, user.getEmail());
			ps.execute();
			return true;
		} catch (Exception e) {
			logger.error("update user info failed, " + user.toJson(), e);
			return false;
		} finally {
			JDBCHelper.close(null, ps);
		}
	}

	private static boolean insertUserIntoDb(User user) {
		Connection conn;
		PreparedStatement ps = null;
		try {
			conn = JDBCHelper.getConnection();
			ps = conn.prepareStatement("insert into user(email,password,gender,province,phone,birthday) values (?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ps.setInt(3, user.getGender());
			ps.setString(4, user.getProvince());
			ps.setString(5, user.getPhone());
			ps.setLong(6, user.getBirthday());
			ps.execute();
			return true;
//			ResultSet rs = ps.getGeneratedKeys();
//			rs.next();
//			return rs.getLong(1);
		} catch (Exception e) {
			logger.error("insert new user failed, " + user.toJson(), e);
			return false;
		} finally {
			JDBCHelper.close(null, ps);
		}
	}
	
	public static String generateUid(String username) {
		return UUID.randomUUID().toString();
	}

}
