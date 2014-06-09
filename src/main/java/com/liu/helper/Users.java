package com.liu.helper;

import java.util.List;

import com.liu.message.User;

public class Users {
	public static boolean init() {
		//TODO
	}
	
	private static boolean initRedisUinfo() {
		
	}
	
	private static List<User> readAllUsers() {
		String sql = "select uid,email,password,gender,province,phone,birthday from user;";
	}
	
}
