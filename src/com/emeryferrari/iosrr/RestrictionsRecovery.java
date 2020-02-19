package com.emeryferrari.iosrr;
import java.security.*;
import java.security.spec.*;
import java.io.*;
public class RestrictionsRecovery {
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			RestrictionsRecovery.printUsage();
		} else if (args.length == 4) {
			String key = "";
			String salt = "";
			if (args[0].equals("-k")) {
				key = args[1];
			} else if (args[0].equals("-key")) {
				key = args[1];
			} else if (args[0].equals("-s")) {
				salt = args[1];
			} else if (args[0].equals("-salt")) {
				salt = args[1];
			} else {
				RestrictionsRecovery.printUsage();
			}
			if (args[2].equals("-k")) {
				if (key.equals("")) {
					key = args[3];
				} else {
					RestrictionsRecovery.printUsage();
				}
			} else if (args[2].equals("-key")) {
				if (key.equals("")) {
					key = args[3];
				} else {
					RestrictionsRecovery.printUsage();
				}
			} else if (args[2].equals("-s")) {
				if (salt.equals("")) {
					salt = args[3];
				} else {
					RestrictionsRecovery.printUsage();
				}
			} else if (args[2].equals("-salt")) {
				if (salt.equals("")) {
					salt = args[3];
				} else {
					RestrictionsRecovery.printUsage();
				}
			} else {
				RestrictionsRecovery.printUsage();
			}
			RestrictionsRecovery.calculate(key, salt);
		} else {
			RestrictionsRecovery.printUsage();
		}
	}
	private static void printUsage() {
		System.out.println("Usage: java RestrictionsRecovery [options]");
		System.out.println("  Options:");
		System.out.println("    -k key, -key key: specifies key to use to brute force passcode");
		System.out.println("    -s salt, -salt salt: specifies salt used to produce key");
		System.out.println("    -h, -help: displays this menu");
		System.out.println("\nOn iOS 7.0 to iOS 11.4.1, the key and salt are found in the com.apple.restrictionspassword.plist file located in /var/mobile/preferences.");
		System.exit(0);
	}
	private static void calculate(String key, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
		System.out.println("Bruteforcing restrictions passcode...");
		String passcode = PasscodeChecker.getPasscode(key, salt);
		if (passcode.length() == 4) {
			System.out.println("Found passcode!");
			System.out.println("Restrictions passcode: " + passcode);
		} else {
			System.out.println("The key " + key + " with the salt " + salt + " does not appear to be a valid combination for any iOS restrictions passcode. Please ensure you have entered the key and salt correctly.");
		}
	}
}