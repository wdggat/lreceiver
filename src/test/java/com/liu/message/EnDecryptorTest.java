package com.liu.message;

import static org.junit.Assert.*;

import org.junit.Test;

import com.liu.helper.Configuration;

public class EnDecryptorTest {
	private static final Configuration conf = new Configuration();
	private static final String KEY = conf.getCryptKey();
	
	@Test
	public void testEncrypt() throws Exception {
		String origText = "{\"appChannel\":\"qq\",\"appVersion\":\"0.1\",\"content\":\"内容_test\",\"costTime\":300,\"dataType\":\"m\",\"deviceModel\":\"小米\",\"deviceNetWork\":\"wifi\",\"deviceOs\":\"android\",\"deviceOsVersion\":\"4.4\",\"deviceUdid\":\"2fasf3qw3er23\",\"msgId\":\"msgid_9527\",\"msgType\":\"email\",\"occurTime\":1394541154503,\"subject\":\"邮件subject_test\",\"to\":[\"wdggat@163.com\"],\"userId\":\"userid_001\"}";
		String encrypted = EnDecryptor.encrypt(origText, KEY);
		System.out.println(encrypted);
		String decrypted = EnDecryptor.decrypt(encrypted, KEY);
		assertEquals(origText, decrypted);
	}
}
