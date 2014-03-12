package com.liu.message;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EnDecryptor {
	public static String decrypt(String src, String key) {
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
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}

	public static String encrypt(String src, String key) throws Exception {
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
		return byte2hex(encrypted).toLowerCase();
		// return encrypted.toString();
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
}
