package com.alyxferrari.iosrr;
public class CommandLineOutput {
	private CommandLineOutput() {}
	public static void printUsage() {
		System.out.println("Usage: java RestrictionsRecovery [options]");
		System.out.println("  Options:");
		System.out.println("    -iproxy [port]: recover the restrictions passcode directly from a connected jailbroken device with OpenSSH installed, with the port specified in the optional port argument; if no port is specified, 22 will be used\n");
		System.out.println("    -ssh ip_address, --secure-shell ip_address: connects to a jailbroken device and recovers the restrictions passcode directly from it; must have OpenSSH installed on the target device");
		System.out.println("    -password password: (optional) specifies SSH password when connecting to device via SSH, if no password is specified, 'alpine' will be used; can also be used with the -iproxy option");
		System.out.println("    -port port: (optional) specifies SSH port when connecting to device via SSH, if no port is specified, 22 will be used; can also be used with the -iproxy option\n");
		System.out.println("    -f file, -file file: reads the key and salt directly from the passcode property list file\n");
		System.out.println("    -k key, -key key: specifies key to use to brute force passcode");
		System.out.println("    -s salt, -salt salt: specifies salt used to produce key");
		System.out.println("    -v, -version: displays the version of RestrictionsRecovery installed");
		System.out.println("    -h, -help: displays this menu");
		System.out.println("\nOn iOS 7.0 to iOS 11.4.1, the key and salt are found in the com.apple.restrictionspassword.plist file located in /private/var/mobile/Library/Preferences.");
		System.exit(0);
	}
	public static void printVersion() {
		System.out.println("RestrictionsRecovery version: v" + RRConst.VERSION);
		System.exit(0);
	}
	public static void printUnsupportedOS() {
		System.out.println("Host operating system is unsupported by iOS Restrictions Recovery.");
		System.exit(0);
	}
	public static void printIproxyError() {
		System.out.println("iproxy encountered a fatal error. Please ensure you have the dependencies listed in the README installed.");
		System.exit(0);
	}
}