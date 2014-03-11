package com.liu.message;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidatorTest {
	@Test
	public void testCheckInputJson() {
		String jsonStr = "{\"appChannel\":\"qq\",\"appVersion\":\"0.1\",\"content\":\"内容_test\",\"costTime\":300,\"dataType\":\"m\",\"deviceModel\":\"小米\",\"deviceNetWork\":\"wifi\",\"deviceOs\":\"android\",\"deviceOsVersion\":\"4.4\",\"deviceUdid\":\"2fasf3qw3er23\",\"msgId\":\"msgid_9527\",\"msgType\":\"email\",\"occurTime\":1394541154503,\"subject\":\"邮件subject_test\",\"to\":[\"to_1\"],\"userId\":\"userid_001\"}";
		assertTrue(Validator.checkInputJson(jsonStr));
	}
}
