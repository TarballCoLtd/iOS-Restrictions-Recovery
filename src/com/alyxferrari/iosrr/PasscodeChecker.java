package com.alyxferrari.iosrr;
import java.util.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.security.*;
import java.security.spec.*;
import java.io.*;
public class PasscodeChecker {
	private PasscodeChecker() {}
	public static String calculateHash(String passcode, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
		char[] passwordChar = passcode.toCharArray();
		byte[] saltByte = Base64.getDecoder().decode(salt.getBytes("UTF-8"));
		PBEKeySpec spec = new PBEKeySpec(passwordChar, saltByte, 1000, 160);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] result = factory.generateSecret(spec).getEncoded();
		return new String(new String(Base64.getEncoder().encode(result)));
	}
	public static String getPasscode(String key, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
		for (int i = 0; i < 10000; i++) {
			String passcode = "" + i;
			int length = Integer.toString(i).length();
			if (length == 1) {
				passcode = "000" + i;
			} else if (length == 2) {
				passcode = "00" + i;
			} else if (length == 3) {
				passcode = "0" + i;
			}
			String result = PasscodeChecker.calculateHash(passcode, salt);
			if (result.contentEquals(key)) {
				return passcode;
			}
		}
		return "";
	}
}