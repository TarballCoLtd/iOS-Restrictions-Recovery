package com.emeryferrari.iosrr;
import java.security.*;
import java.security.spec.*;
import java.io.*;
import net.schmizz.sshj.*;
import net.schmizz.sshj.transport.verification.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
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
			} else if (args[0].equals("-iproxy")) {
				RestrictionsRecovery.launchIproxy(22, "alpine");
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
		} else if (args.length == 3) {
			if (args[0].equals("-iproxy")) {
				if (args[1].equals("-port")) {
					RestrictionsRecovery.launchIproxy(Integer.parseInt(args[2]), "alpine");
				} else if (args[1].equals("-password")) {
					RestrictionsRecovery.launchIproxy(22, args[2]);
				} else {
					RestrictionsRecovery.printUsage();
				}
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
				KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist("password.plist");
				RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt());
			}
		} else if (args.length == 5) {
			if (args[0].equals("-iproxy")) {
				if (args[1].equals("-port")) {
					if (args[3].equals("-password")) {
						RestrictionsRecovery.launchIproxy(Integer.parseInt(args[2]), args[4]);
					} else {
						RestrictionsRecovery.printUsage();
					}
				} else if (args[1].equals("-password")) {
					if (args[3].equals("-port")) {
						RestrictionsRecovery.launchIproxy(Integer.parseInt(args[4]), args[2]);
					} else {
						RestrictionsRecovery.printUsage();
					}
				} else {
					RestrictionsRecovery.printUsage();
				}
			} else {
				RestrictionsRecovery.printUsage();
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
				KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist("password.plist");
				RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt());
			}
		} else {
			KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist(file);
			RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt());
		}
	}
	private static void printUsage() {
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
		if (RestrictionsRecovery.identifyHostOS() != OperatingSystem.OTHER) {
			SSHClient ssh = new SSHClient();
			ssh.addHostKeyVerifier(new PromiscuousVerifier());
			ssh.loadKnownHosts();
			ssh.connect(ip, port);
			ssh.authPassword("root", password);
			ssh.newSCPFileTransfer().download("/private/var/mobile/Library/Preferences/com.apple.restrictionspassword.plist", "password.plist");
			ssh.disconnect();
			ssh.close();
			System.exit(0);
		} else {
			RestrictionsRecovery.printUnsupportedOS();
		}
	}
	private static OperatingSystem identifyHostOS() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			return OperatingSystem.WINDOWS;
		} else if (os.indexOf("mac") >= 0) {
			if (System.getProperty("os.version").indexOf("10.15") >= 0) {
				return OperatingSystem.MACOSCATALINA;
			} else {
				return OperatingSystem.MACOSMOJAVE;
			}
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
	private static void printUnsupportedOS() {
		System.out.println("Host operating system is unsupported by iOS Restrictions Recovery.");
		System.exit(0);
	}
	private static void launchIproxy(int port, String password) throws IOException, SAXException, ParserConfigurationException, InvalidKeySpecException, NoSuchAlgorithmException {
		ProcessBuilder builder = null;
		if (RestrictionsRecovery.identifyHostOS() == OperatingSystem.MACOSMOJAVE || RestrictionsRecovery.identifyHostOS() == OperatingSystem.UNIX) {
			builder = new ProcessBuilder("/bin/bash", "-c", "iproxy", "23", ""+port);
		} else if (RestrictionsRecovery.identifyHostOS() == OperatingSystem.MACOSCATALINA) {
			builder = new ProcessBuilder("/bin/zsh", "-c", "iproxy", "23", ""+port);
		} else {
			RestrictionsRecovery.printUnsupportedOS();
		}
		builder.redirectErrorStream(true);
		Process process = builder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = reader.readLine();
		if (line.equals("waiting for connection")) {
			RestrictionsRecovery.downloadViaSSH("127.0.0.1", 23, password);
			KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist("password.plist");
			RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt());
		} else {
			RestrictionsRecovery.printIproxyError();
		}
	}
	private static void printIproxyError() {
		System.out.println("iproxy encountered a fatal error. Please ensure you have the dependencies listed in the README installed.");
		System.exit(0);
	}
}