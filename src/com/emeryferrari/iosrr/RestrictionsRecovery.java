package com.emeryferrari.iosrr;
import java.security.*;
import java.security.spec.*;
import java.io.*;
import net.schmizz.sshj.*;
import net.schmizz.sshj.transport.verification.*;
public class RestrictionsRecovery {
	private RestrictionsRecovery() {}
	public static void main(String[] args) throws Exception {
		String file = "";
		String sshIP = "";
		String password = "alpine";
		int port = 22;
		if (args.length == 0) {
			RestrictionsRecovery.printUsage();
		} else if (args.length == 1) {
			if (args[0].equals("-v")) {
				RestrictionsRecovery.printVersion();
			} else if (args[0].equals("-version")) {
				RestrictionsRecovery.printVersion();
			} else {
				RestrictionsRecovery.printUsage();
			}
		} else if (args.length == 2) {
			if (args[0].equals("-f")) {
				file = args[1];
			} else if (args[0].equals("-file")) {
				file = args[1];
			} else if (args[0].equals("-ssh")) {
				sshIP = args[1];
			} else if (args[0].equals("--secure-shell")) {
				sshIP = args[1];
			} else {
				RestrictionsRecovery.printUsage();
			}
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
			} else if (args[0].equals("-ssh")) {
				sshIP = args[1];
			} else if (args[0].equals("--secure-shell")) {
				sshIP = args[1];
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
			} else if (args[2].equals("-password")) {
				password = args[3];
			} else if (args[2].equals("-port")) {
				port = Integer.parseInt(args[3]);
			} else {
				RestrictionsRecovery.printUsage();
			}
			if (sshIP.equals("")) {
				RestrictionsRecovery.calculate(key, salt);
			} else {
				RestrictionsRecovery.downloadViaSSH(sshIP, port, password);
			}
		} else if (args.length == 6) {
			if (args[0].equals("-ssh")) {
				sshIP = args[1];
				if (args[2].equals("-password")) {
					password = args[3];
					if (args[4].equals("-port")) {
						port = Integer.parseInt(args[5]);
					} else {
						RestrictionsRecovery.printUsage();
					}
				} else if (args[2].equals("-port")) {
					port = Integer.parseInt(args[3]);
					if (args[4].equals("-password")) {
						password = args[5];
					} else {
						RestrictionsRecovery.printUsage();
					}
				} else {
					RestrictionsRecovery.printUsage();
				}
			} else if (args[0].equals("--secure-shell")) {
				sshIP = args[1];
				if (args[2].equals("-password")) {
					password = args[3];
					if (args[4].equals("-port")) {
						port = Integer.parseInt(args[5]);
					} else {
						RestrictionsRecovery.printUsage();
					}
				} else if (args[2].equals("-port")) {
					port = Integer.parseInt(args[3]);
					if (args[4].equals("-password")) {
						password = args[5];
					} else {
						RestrictionsRecovery.printUsage();
					}
				} else {
					RestrictionsRecovery.printUsage();
				}
			} else {
				RestrictionsRecovery.printUsage();
			}
		} else {
			RestrictionsRecovery.printUsage();
		}
		if (file.equals("")) {
			if (!(sshIP.equals(""))) {
				RestrictionsRecovery.downloadViaSSH(sshIP, port, password);
			}
		} else {
			KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist(file);
			RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt());
		}
	}
	private static void printUsage() {
		System.out.println("Usage: java RestrictionsRecovery [options]");
		System.out.println("  Options:");
		System.out.println("    -ssh ip_address, --secure-shell ip_address: connects to a jailbroken device and recovers the restrictions passcode directly from it; must have OpenSSH installed on the target device");
		System.out.println("    -password password: (optional) specifies SSH password when connecting to device via SSH, if no password is specified, 'alpine' will be used");
		System.out.println("    -port port: (optional) specifies SSH port when connecting to device via SSH, if no port is specified, 22 will be used\n");
		System.out.println("    -f file, -file file: reads the key and salt directly from the passcode property list file\n");
		System.out.println("    -k key, -key key: specifies key to use to brute force passcode");
		System.out.println("    -s salt, -salt salt: specifies salt used to produce key");
		System.out.println("    -v, -version: displays the version of RestrictionsRecovery installed");
		System.out.println("    -h, -help: displays this menu");
		System.out.println("\nOn iOS 7.0 to iOS 11.4.1, the key and salt are found in the com.apple.restrictionspassword.plist file located in /private/var/mobile/Library/Preferences.");
		System.exit(0);
	}
	private static void printVersion() {
		System.out.println("RestrictionsRecovery version: v" + RRConst.VERSION);
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
		System.exit(0);
	}
	private static void downloadViaSSH(String ip, int port, String password) throws IOException {
		if (RestrictionsRecovery.identifyHostOS() == OperatingSystem.WINDOWS) {
			SSHClient ssh = new SSHClient();
			ssh.addHostKeyVerifier(new PromiscuousVerifier());
			ssh.loadKnownHosts();
			ssh.connect(ip, port);
			ssh.authPassword("root", password);
			ssh.newSCPFileTransfer().download("/private/var/mobile/Library/Preferences/com.apple.restrictionspassword.plist", "password.plist");
			ssh.disconnect();
			ssh.close();
		}
		System.exit(0);
	}
	private static OperatingSystem identifyHostOS() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			return OperatingSystem.WINDOWS;
		} else if (os.indexOf("mac") >= 0) {
			return OperatingSystem.MACOS;
		} else if (os.indexOf("nix") >= 0) {
			return OperatingSystem.UNIX;
		} else if (os.indexOf("nux") >= 0) {
			return OperatingSystem.UNIX;
		} else if (os.indexOf("aix") >= 0) {
			return OperatingSystem.UNIX;
		} else {
			return OperatingSystem.OTHER;
		}
	}
}