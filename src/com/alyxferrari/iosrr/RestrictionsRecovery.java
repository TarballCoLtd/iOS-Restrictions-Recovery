package com.alyxferrari.iosrr;
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
		if (args.length == 0) {
			Display.createDisplay();
		} else {
			RRArguments arguments = ArgumentParser.parseArguments(args);
			if (arguments.getRequestType() == RequestType.IPROXY) {
				int port = 22;
				String password = "alpine";
				if (arguments.getPort() != null) {
					port = Integer.parseInt(arguments.getPort());
				}
				if (arguments.getPassword() != null) {
					password = arguments.getPassword();
				}
				RestrictionsRecovery.launchIproxy(port, password, true);
			} else if (arguments.getRequestType() == RequestType.SSH) {
				int port = 22;
				String password = "alpine";
				if (arguments.getPort() != null) {
					port = Integer.parseInt(arguments.getPort());
				}
				if (arguments.getPassword() != null) {
					password = arguments.getPassword();
				}
				RestrictionsRecovery.downloadViaSSH(arguments.getSsh(), port, password, true);
				KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist("password.plist");
				RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), true);
			} else if (arguments.getRequestType() == RequestType.FILE) {
				KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist(arguments.getFile());
				RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), true);
			} else if (arguments.getRequestType() == RequestType.KEYSALT) {
				RestrictionsRecovery.calculate(arguments.getKey(), arguments.getSalt(), true);
			} else if (arguments.getRequestType() == RequestType.VERSION) {
				CommandLineOutput.printVersion();
			} else {
				CommandLineOutput.printUsage();
			}
		}
	}
	public static String calculate(String key, String salt, boolean exit) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
		System.out.println("Bruteforcing restrictions passcode...");
		String passcode = PasscodeChecker.getPasscode(key, salt);
		if (passcode.length() == 4) {
			System.out.println("Found passcode!");
			System.out.println("Restrictions passcode: " + passcode);
			if (exit) {
				System.exit(0);
			}
			return passcode;
		} else {
			System.out.println("The key " + key + " with the salt " + salt + " does not appear to be a valid combination for any iOS restrictions passcode. Please ensure you have entered the key and salt correctly.");
			if (exit) {
				System.exit(0);
			}
			return null;
		}
	}
	public static void downloadViaSSH(String ip, int port, String password, boolean exit) throws IOException {
		if (RestrictionsRecovery.identifyHostOS() != OperatingSystemType.OTHER) {
			SSHClient ssh = new SSHClient();
			ssh.addHostKeyVerifier(new PromiscuousVerifier());
			ssh.connect(ip, port);
			ssh.authPassword("root", password);
			ssh.newSCPFileTransfer().download("/private/var/mobile/Library/Preferences/com.apple.restrictionspassword.plist", "password.plist");
			ssh.disconnect();
			ssh.close();
			if (exit) {
				System.exit(0);
			}
		} else {
			CommandLineOutput.printUnsupportedOS();
		}
	}
	public static OperatingSystemType identifyHostOS() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			return OperatingSystemType.WINDOWS;
		} else if (os.indexOf("mac") >= 0) {
			if (System.getProperty("os.version").indexOf("10.15") >= 0) {
				return OperatingSystemType.MACOSCATALINA_OR_NEWER;
			} else if (System.getProperty("os.version").indexOf("11.") >= 0) {
				return OperatingSystemType.MACOSCATALINA_OR_NEWER;
			} else {
				return OperatingSystemType.MACOSMOJAVE_OR_OLDER;
			}
		} else if (os.indexOf("nix") >= 0) {
			return OperatingSystemType.UNIX_BASED;
		} else if (os.indexOf("nux") >= 0) {
			return OperatingSystemType.UNIX_BASED;
		} else if (os.indexOf("aix") >= 0) {
			return OperatingSystemType.UNIX_BASED;
		} else {
			return OperatingSystemType.OTHER;
		}
	}
	public static void launchIproxy(int port, String password, boolean exit) throws IOException, SAXException, ParserConfigurationException, InvalidKeySpecException, NoSuchAlgorithmException {
		ProcessBuilder builder = null;
		if (RestrictionsRecovery.identifyHostOS() == OperatingSystemType.MACOSMOJAVE_OR_OLDER || RestrictionsRecovery.identifyHostOS() == OperatingSystemType.UNIX_BASED) {
			builder = new ProcessBuilder("/bin/bash", "-c", "iproxy", "23", ""+port);
		} else if (RestrictionsRecovery.identifyHostOS() == OperatingSystemType.MACOSCATALINA_OR_NEWER) {
			builder = new ProcessBuilder("/bin/zsh", "-c", "iproxy", "23", ""+port);
		} else if (RestrictionsRecovery.identifyHostOS() == OperatingSystemType.WINDOWS) {
			builder = new ProcessBuilder("cmd.exe", "/c", "iproxy", "23", ""+port); // MAKE SURE YOU HAVE IPROXY IN YOUR PATH ENVIRONMENT VARIABLE
		} else {
			CommandLineOutput.printUnsupportedOS();
		}
		builder.redirectErrorStream(true);
		Process process = builder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = reader.readLine();
		if (line.equals("waiting for connection")) {
			RestrictionsRecovery.downloadViaSSH("127.0.0.1", 23, password, exit);
			KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist("password.plist");
			RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), exit);
		} else {
			CommandLineOutput.printIproxyError();
		}
	}
}