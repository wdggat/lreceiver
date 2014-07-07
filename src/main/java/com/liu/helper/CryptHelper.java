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

import org.apache.log4j.Logger;

public class CryptHelper {
	private static final Logger logger = Logger.getLogger(CryptHelper.class);
	
	public static String decrypt(String src, String key) {
		byte content[] = aesDecrypt(src, key);
		if(content == null)
			return null;
		return gzipDecompress(content); 
	}
	
	public static String encrypt(String src, String key) {
		String compressed = gzipCompress(src.getBytes());
		if(compressed == null)
			return null;
		byte[] encrypted = aesEncrypt(compressed, key);
		return encrypted == null ? null : new String(encrypted);
	}
	
	private static byte[] aesDecrypt(String src, String key) {
		try {
			if (key == null) {
				return null;
			}
			if (key.length() != 16) {
				return null;
			}
			byte[] raw = key.getBytes("ASCII");
			SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] encrypted1 = hex2byte(src);
			byte[] original = cipher.doFinal(encrypted1);
			return original;
		} catch (Exception ex) {
			logger.error("Exception when decrypt text: " + src + ", KEY: " + key);
			return null;
		}
	}
	
	private static byte[] aesEncrypt(String src, String key) {
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
			byte[] encrypted = cipher.doFinal(src.getBytes());
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
	
	private static String gzipDecompress(byte[] data) {
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
	
	private static String gzipCompress(byte[] content) {
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
		return new String(data);
	}
	
	private static String inputStream2String(InputStream is) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	private static byte[] SHA1(String content) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			return md.digest(content.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
