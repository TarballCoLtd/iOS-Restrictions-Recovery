package com.alyxferrari.iosrr;
public class KeySaltPair {
	private final String key;
	private final String salt;
	public KeySaltPair(String key, String salt) {
		this.key = key;
		this.salt = salt;
	}
	public String getKey() {
		return key;
	}
	public String getSalt() {
		return salt;
	}
}