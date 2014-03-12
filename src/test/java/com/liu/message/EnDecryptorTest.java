package com.liu.message;

import static org.junit.Assert.*;

import org.junit.Test;

import com.liu.helper.Configuration;

public class EnDecryptorTest {
	private static final Configuration conf = new Configuration();
	private static final String KEY = conf.getCryptKey();
	
	@Test
	public void testEncrypt() throws Exception {
		String origText = "abcde12345";
		String encrypted = EnDecryptor.encrypt(origText, KEY);
		System.out.println(encrypted);
		String decrypted = EnDecryptor.decrypt(encrypted, KEY);
		assertEquals(origText, decrypted);
	}
}
