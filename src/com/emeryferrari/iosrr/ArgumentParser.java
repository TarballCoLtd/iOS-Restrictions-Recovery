package com.emeryferrari.iosrr;
public class ArgumentParser {
	private ArgumentParser() {}
	public static RRArguments parseArguments(String[] args) {
		boolean iproxy = false;
		String ssh = null;
		String password = null;
		String port = null;
		String file = null;
		String key = null;
		String salt = null;
		boolean version = false;
		for (int i = 0; i < args.length; i++) {
			try {
				if (args[i].equals("-iproxy")) {
					iproxy = true;
				} else if (args[i].equals("-ssh")) {
					i++;
					ssh = args[i];
				} else if (args[i].equals("--secure-shell")) {
					i++;
					ssh = args[i];
				} else if (args[i].equals("-password")) {
					i++;
					password = args[i];
				} else if (args[i].equals("-port")) {
					i++;
					port = args[i];
				} else if (args[i].equals("-f")) {
					i++;
					file = args[i];
				} else if (args[i].equals("-file")) {
					i++;
					file = args[i];
				} else if (args[i].equals("-k")) {
					i++;
					key = args[i];
				} else if (args[i].equals("-key")) {
					i++;
					key = args[i];
				} else if (args[i].equals("-s")) {
					i++;
					salt = args[i];
				} else if (args[i].equals("-salt")) {
					i++;
					salt = args[i];
				} else if (args[i].equals("-v")) {
					version = true;
				} else if (args[i].equals("-version")) {
					version = true;
				}
			} catch (IndexOutOfBoundsException ex) {
				return new RRArguments(false, null, null, null, null, null, null, false);
			}
		}
		return new RRArguments(iproxy, ssh, password, port, file, key, salt, version);
	}
}