package com.liu.message;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValidatorTest {
	@Test
	public void testCheckInputJson() {
//		String jsonStr = "{\"appChannel\":\"qq\",\"appVersion\":\"0.1\",\"content\":\"内容_test\",\"costTime\":300,\"dataType\":\"m\",\"deviceModel\":\"小米\",\"deviceNetWork\":\"wifi\",\"deviceOs\":\"android\",\"deviceOsVersion\":\"4.4\",\"deviceUdid\":\"2fasf3qw3er23\",\"msgId\":\"msgid_9527\",\"msgType\":\"email\",\"occurTime\":1394541154503,\"subject\":\"邮件subject_test\",\"to\":[\"to_1\"],\"userId\":\"userid_001\"}";
		String jsonStr = "{\"dataType\":\"LOGIN\", \"jsonStr\":\"{\\\"a\\\":2}\"}";
		assertTrue(Validator.checkInputJson(jsonStr));
	}
	
	@Test
	public void testRequestToString() {
		Event event = new Event(DataType.LOGIN);
		event.putEntry("hello", "boy");
		Request request = new Request(DataType.LOGIN, event.toJson());
		String expected = "{\"dataType\":\"LOGIN\",\"jsonStr\":\"{\\\"dataType\\\":\\\"LOGIN\\\",\\\"entrys\\\":{\\\"hello\\\":\\\"boy\\\"}}\"}";
		System.out.println(request.toJson());
		assertEquals(expected, request.toJson());
	}
}
