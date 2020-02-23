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
			RestrictionsRecovery.launchIproxy(port, password);
		} else if (arguments.getRequestType() == RequestType.SSH) {
			int port = 22;
			String password = "alpine";
			if (arguments.getPort() != null) {
				port = Integer.parseInt(arguments.getPort());
			}
			if (arguments.getPassword() != null) {
				password = arguments.getPassword();
			}
			RestrictionsRecovery.downloadViaSSH(arguments.getSsh(), port, password);
		} else if (arguments.getRequestType() == RequestType.FILE) {
			KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist(arguments.getFile());
			RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt());
		} else if (arguments.getRequestType() == RequestType.KEYSALT) {
			RestrictionsRecovery.calculate(arguments.getKey(), arguments.getSalt());
		} else if (arguments.getRequestType() == RequestType.VERSION) {
			CommandLineOutput.printVersion();
		} else {
			CommandLineOutput.printUsage();
		}
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
			CommandLineOutput.printUnsupportedOS();
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
	private static void launchIproxy(int port, String password) throws IOException, SAXException, ParserConfigurationException, InvalidKeySpecException, NoSuchAlgorithmException {
		ProcessBuilder builder = null;
		if (RestrictionsRecovery.identifyHostOS() == OperatingSystem.MACOSMOJAVE || RestrictionsRecovery.identifyHostOS() == OperatingSystem.UNIX) {
			builder = new ProcessBuilder("/bin/bash", "-c", "iproxy", "23", ""+port);
		} else if (RestrictionsRecovery.identifyHostOS() == OperatingSystem.MACOSCATALINA) {
			builder = new ProcessBuilder("/bin/zsh", "-c", "iproxy", "23", ""+port);
		} else if (RestrictionsRecovery.identifyHostOS() == OperatingSystem.WINDOWS) {
			
		} else {
			CommandLineOutput.printUnsupportedOS();
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
			CommandLineOutput.printIproxyError();
		}
	}
}