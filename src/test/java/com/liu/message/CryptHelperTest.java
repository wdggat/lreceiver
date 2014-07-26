package com.liu.message;

import static org.junit.Assert.*;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liu.helper.Configuration;
import com.liu.helper.CryptHelper;

public class CryptHelperTest {
	private static final Configuration conf = new Configuration();
	private static final String KEY = conf.getCryptKey();
	
	@BeforeClass
	public static void setUp() {
		PropertyConfigurator.configure(Configuration.DEFAULT_CONF_PATH);
	}
	
	@Test
	public void testEncrypt() throws Exception {
		String origText = "{\"appChannel\":\"qq\",\"appVersion\":\"0.1\",\"content\":\"内容_test\",\"costTime\":300,\"dataType\":\"m\",\"deviceModel\":\"小米\",\"deviceNetWork\":\"wifi\",\"deviceOs\":\"android\",\"deviceOsVersion\":\"4.4\",\"deviceUdid\":\"2fasf3qw3er23\",\"msgId\":\"msgid_9527\",\"msgType\":\"email\",\"occurTime\":1394541154503,\"subject\":\"邮件subject_test\",\"to\":[\"wdggat@163.com\"],\"userId\":\"userid_001\"}";
		byte[] encrypted = CryptHelper.encrypt(origText, KEY);
		String decrypted = CryptHelper.decrypt(encrypted, KEY);
		System.out.println(decrypted);
		assertEquals(origText, decrypted);
	}
	
	@Test
	public void testGzipCompress() {
		String origText = "{\"appChannel\":\"qq\",\"appVersion\":\"0.1\",\"content\":\"内容_test\",\"costTime\":300,\"dataType\":\"m\",\"deviceModel\":\"小米\",\"deviceNetWork\":\"wifi\",\"deviceOs\":\"android\",\"deviceOsVersion\":\"4.4\",\"deviceUdid\":\"2fasf3qw3er23\",\"msgId\":\"msgid_9527\",\"msgType\":\"email\",\"occurTime\":1394541154503,\"subject\":\"邮件subject_test\",\"to\":[\"wdggat@163.com\"],\"userId\":\"userid_001\"}";
		byte[] compressed = CryptHelper.gzipCompress(origText.getBytes());
		System.out.println(origText.length() + "\t" + compressed.length + "\t" + compressed);
		String decompressed = CryptHelper.gzipDecompress(compressed);
		assertEquals(origText, decompressed);
	}
	
	@Test
	public void testBase64() {
		String origText = "{\"appChannel\":\"qq\",\"appVersion\":\"0.1\",\"content\":\"内容_test\",\"costTime\":300,\"dataType\":\"m\",\"deviceModel\":\"小米\",\"deviceNetWork\":\"wifi\",\"deviceOs\":\"android\",\"deviceOsVersion\":\"4.4\",\"deviceUdid\":\"2fasf3qw3er23\",\"msgId\":\"msgid_9527\",\"msgType\":\"email\",\"occurTime\":1394541154503,\"subject\":\"邮件subject_test\",\"to\":[\"wdggat@163.com\"],\"userId\":\"userid_001\"}";
		byte[] origdata = origText.getBytes();
		System.out.println(origdata.length);
		byte[] based = CryptHelper.base64Encode(origdata);
		System.out.println(based.length);
		byte[] unbased = CryptHelper.base64Decode(based);
		System.out.println(new String(origdata));
		assertArrayEquals(origdata, unbased);
	}
}
