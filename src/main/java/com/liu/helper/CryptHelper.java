package com.liu.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class CryptHelper {
	private static final Logger logger = Logger.getLogger(CryptHelper.class);
	
	public static String decrypt(byte[] src, String key) {
		byte content[] = aesDecrypt(src, key);
		if(content == null)
			return null;
		return gzipDecompress(content); 
	}
	
	public static byte[] encrypt(String src, String key) {
		byte[] compressed = gzipCompress(src.getBytes());
		if(compressed == null)
			return null;
		byte[] encrypted = aesEncrypt(compressed, key);
		return encrypted;
	}
	
	private static byte[] aesDecrypt(byte[] src, String key) {
		try {
			if (key == null) {
				return null;
			}
			if (key.length() != 16) {
				return null;
			}
			byte[] raw = key.getBytes("ASCII");
			SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] original = cipher.doFinal(src);
			return original;
		} catch (Exception ex) {
			logger.error("Exception when decrypt text: " + src + ", KEY: " + key, ex);
			return null;
		}
	}
	
	private static byte[] aesEncrypt(byte[] src, String key) {
		try {
			if (key == null) {
				return null;
			}
			if (key.length() != 16) {
				return null;
			}
			byte[] raw = key.getBytes();
			SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] encrypted = cipher.doFinal(src);
			return encrypted;
		} catch (Exception ex) {
			logger.error("Exception when encrypt text: " + src + ", KEY: "
					+ key);
			return null;
		}
	}

	private static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}

	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
	
	public static String gzipDecompress(byte[] data) {
		ByteArrayInputStream b = new ByteArrayInputStream(data);
		try {
			GZIPInputStream ginstream = new GZIPInputStream(b);
			return inputStream2String(ginstream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] gzipCompress(byte[] content) {
		byte[] data = null;
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		try {
			GZIPOutputStream gzipOut = new GZIPOutputStream(b, content.length);
			gzipOut.write(content);
			gzipOut.flush();
			gzipOut.close();
			data = b.toByteArray();
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private static String inputStream2String(InputStream is) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}
	
	@SuppressWarnings("unused")
	private static byte[] SHA1(String content) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			return md.digest(content.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] base64Encode(byte[] content) {
		return Base64.encodeBase64(content);
	}
	
	public static byte[] base64Decode(byte[] content) {
		return Base64.decodeBase64(content);
	}
}
