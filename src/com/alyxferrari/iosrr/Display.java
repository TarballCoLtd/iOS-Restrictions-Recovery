package com.alyxferrari.iosrr;
import javax.swing.*;
import java.net.*;
import java.awt.event.*;
import java.io.*;
import net.schmizz.sshj.*;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.transport.verification.*;
import net.schmizz.sshj.connection.channel.direct.*;
import org.apache.commons.io.*;
public class Display {
	private Display() {}
	private static final Display CLASS_OBJ = new Display();
	private static JFrame FRAME = new JFrame(RRConst.FULL_NAME);
	private static JLabel AUTHOR = new JLabel("by " + RRConst.AUTHOR);
	private static JLabel TITLE = new JLabel(RRConst.TITLE);
	private static JLabel DESC = new JLabel(RRConst.DESC);
	private static JButton KEY_SALT_BUTTON = new JButton(RRConst.KEY_SALT_BUTTON);
	private static JButton FILE_BUTTON = new JButton(RRConst.FILE_BUTTON);
	private static JButton SSH_BUTTON = new JButton(RRConst.SSH_BUTTON);
	private static JButton IPROXY_BUTTON = new JButton(RRConst.IPROXY_BUTTON);
	private static JButton ITUNES_BACKUP = new JButton(RRConst.ITUNES_BACKUP);
	private static JButton KEYCHAIN_DUMPER = new JButton(RRConst.KEYCHAIN_DUMPER);
	private static JButton ABOUT = new JButton(RRConst.ABOUT);
	private static JLabel iOS_13 = new JLabel(RRConst.iOS_13);
	private static JLabel iOS_11 = new JLabel(RRConst.iOS_11);
	private static InfoPlist[] plists = null;
	private static boolean initialized = false;
	public static void createDisplay() {
		if (!initialized) {
			Display.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Display.FRAME.setSize(800, 600);
			Display.FRAME.getContentPane().setLayout(new BoxLayout(FRAME.getContentPane(), BoxLayout.Y_AXIS));
			Display.KEY_SALT_BUTTON.addActionListener(Display.CLASS_OBJ.new KeySaltButtonListener());
			Display.FILE_BUTTON.addActionListener(Display.CLASS_OBJ.new FileButtonListener());
			Display.SSH_BUTTON.addActionListener(Display.CLASS_OBJ.new SSHButtonListener());
			Display.KEYCHAIN_DUMPER.addActionListener(Display.CLASS_OBJ.new KeychainDumperListener());
			Display.IPROXY_BUTTON.addActionListener(Display.CLASS_OBJ.new IproxyButtonListener());
			Display.IPROXY_BUTTON.setEnabled(false);
			Display.ITUNES_BACKUP.addActionListener(Display.CLASS_OBJ.new ItunesBackupListener());
			Display.ABOUT.addActionListener(Display.CLASS_OBJ.new AboutListener());
			initialized = true;
		}
		Display.FRAME.getContentPane().add(Display.TITLE);
		Display.FRAME.getContentPane().add(Display.AUTHOR);
		Display.FRAME.getContentPane().add(Display.DESC);
		Display.FRAME.getContentPane().add(Display.ABOUT);
		Display.FRAME.getContentPane().add(Display.iOS_11);
		Display.FRAME.getContentPane().add(Display.KEY_SALT_BUTTON);
		Display.FRAME.getContentPane().add(Display.FILE_BUTTON);
		Display.FRAME.getContentPane().add(Display.SSH_BUTTON);
		Display.FRAME.getContentPane().add(Display.ITUNES_BACKUP);
		Display.FRAME.getContentPane().add(Display.iOS_13);
		Display.FRAME.getContentPane().add(Display.KEYCHAIN_DUMPER);
		Display.FRAME.setVisible(true);
	}
	public class AboutListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			Display.FRAME.getContentPane().removeAll();
			JLabel title = new JLabel(RRConst.FULL_NAME);
			JLabel author = new JLabel("by " + RRConst.AUTHOR);
			JLabel slf4j = new JLabel("<html><body><br/>slf4j Copyright (c) 2004-2017 QOS.ch</body></html>");
			JLabel bcJava = new JLabel("bc-java Copyright (c) 2000-2019 The Legion of the Bouncy Castle Inc.");
			JLabel sshj = new JLabel("sshj Copyright (c) 2010-2012 sshj contributors");
			JLabel keychainDumper = new JLabel("keychain_dumper was written by ptoomey3 on GitHub");
			JLabel backup = new JLabel("The iTunes backup idea was given to me by Reddit users u/Starwarsfan2099 and u/KuroAMK");
			JLabel help = new JLabel("<html><body><br/>Testers:<br/>Jacob Ward (u/jacobward328)</body></html>");
			JLabel paypal = new JLabel("<html><body><br/>Special thanks to my PayPal backers:<br/>Jacob Ward<br/>paypal.me/alyxferrari</body></html>");
			OperatingSystemType ops = RestrictionsRecovery.identifyHostOS();
			JLabel os = new JLabel("<html><body><br/>Host OS: Unknown");
			String version = System.getProperty("os.version");
			if (ops == OperatingSystemType.WINDOWS) {
				os = new JLabel("<html><body><br/>Host OS: Windows NT " + version);
			} else if (ops == OperatingSystemType.MACOS_MOJAVE_OR_OLDER || ops == OperatingSystemType.MACOS_CATALINA_OR_NEWER) {
				os = new JLabel("<html><body><br/>Host OS: macOS " + version);
			} else if (ops == OperatingSystemType.UNIX_LIKE) {
				os = new JLabel("<html><body><br/>Host OS: " + System.getProperty("os.name") + " " + version);
			}
			JButton back = new JButton("Back");
			back.addActionListener(new BackListener());
			Display.FRAME.getContentPane().add(title);
			Display.FRAME.getContentPane().add(author);
			Display.FRAME.getContentPane().add(slf4j);
			Display.FRAME.getContentPane().add(bcJava);
			Display.FRAME.getContentPane().add(sshj);
			Display.FRAME.getContentPane().add(keychainDumper);
			Display.FRAME.getContentPane().add(backup);
			Display.FRAME.getContentPane().add(help);
			Display.FRAME.getContentPane().add(paypal);
			Display.FRAME.getContentPane().add(os);
			Display.FRAME.getContentPane().add(back);
			Display.refresh();
		}
	}
	public class KeychainDumperListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			new Thread() {
				@Override
				public void run() {
					Display.FRAME.getContentPane().removeAll();
					String keychain = null;
					try {
						JOptionPane.showMessageDialog(null, "Make sure your device meets the following conditions before proceeding:\nYour device must be jailbroken\nYour device must have an SSH server running\nYour device must have the \"SQLite 3.x\" package installed from Sam Bingner's repo\nYour device is highly recommended to have a passcode and fingerprint/face (it may work without, but having one fixes a lot of issues)\nMake sure your device is unlocked and on the home screen throughout the whole process");
						String ip = JOptionPane.showInputDialog("Device IP address?");
						String portStr = JOptionPane.showInputDialog("Device SSH server port? (press enter to default to 22)");
						int port = 22;
						if (!portStr.equals("")) {
							port = Integer.parseInt(portStr);
						}
						String rootPass = JOptionPane.showInputDialog("What is your device's root password? (press enter to default to 'alpine')");
						if (rootPass.equals("")) {
							rootPass = "alpine";
						}
						File keychain_dumper = new File("keychain_dumper");
						URL keychain_dumperURL = new URL("https://alyxferrari.github.io/keychain_dumper");
						File entitlements = new File("ent.xml");
						URL entitlementsURL = new URL("https://alyxferrari.github.io/ent.xml");
						if (!keychain_dumper.exists()) {
							Display.FRAME.getContentPane().add(new JLabel("Couldn't find keychain_dumper!"));
							Display.FRAME.getContentPane().add(new JLabel("Downloading keychain_dumper from alyxferrari.github.io..."));
							System.out.println("Couldn't find keychain_dumper!");
							System.out.println("Downloading keychain_dumper from alyxferrari.github.io...");
							Display.refresh();
							FileUtils.copyURLToFile(keychain_dumperURL, keychain_dumper);
						}
						if (!entitlements.exists()) {
							Display.FRAME.getContentPane().add(new JLabel("Couldn't find ent.xml!"));
							Display.FRAME.getContentPane().add(new JLabel("Downloading ent.xml from alyxferrari.github.io..."));
							System.out.println("Couldn't find entitlements.xml!");
							System.out.println("Downloading entitlements.xml from alyxferrari.github.io...");
							Display.refresh();
							FileUtils.copyURLToFile(entitlementsURL, entitlements);
						}
						Display.FRAME.getContentPane().add(new JLabel("Connecting to " + ip + ":" + port + " over SSH..."));
						Display.refresh();
						System.out.println("Connecting to " + ip + ":" + port + " over SSH...");
						SSHClient ssh = new SSHClient();
						ssh.addHostKeyVerifier(new PromiscuousVerifier());
						ssh.connect(ip, port);
						Display.FRAME.getContentPane().add(new JLabel("Logging in as user 'root'..."));
						System.out.println("Logging in as user 'root'...");
						Display.refresh();
						ssh.authPassword("root", rootPass);
						Display.FRAME.getContentPane().add(new JLabel("Uploading keychain_dumper to device..."));
						Display.refresh();
						System.out.println("Uploading keychain_dumper to device...");
						ssh.newSCPFileTransfer().upload("keychain_dumper", "/User/Documents/keychain_dumper");
						Display.refresh();
						System.out.println("Uploading ent.xml to device...");
						ssh.newSCPFileTransfer().upload("ent.xml", "/User/Documents/ent.xml");
						Session session = ssh.startSession();
						Display.FRAME.getContentPane().add(new JLabel("Setting keychain_dumper to executable..."));
						System.out.println("Giving keychain_dumper '+x' permissions...");
						Display.refresh();
						session.exec("chmod +x /User/Documents/keychain_dumper");
						session = ssh.startSession();
						Display.FRAME.getContentPane().add(new JLabel("Assigning entitlements to keychain_dumper..."));
						Display.refresh();
						System.out.println("Assigning entitlements to keychain_dumper...");
						session.exec("ldid -S/User/Documents/ent.xml /User/Documents/keychain_dumper");
						Display.FRAME.getContentPane().add(new JLabel("Disconnecting..."));
						System.out.println("Disconnecting...");
						Display.refresh();
						ssh.disconnect();
						ssh.close();
						SSHClient ssh2 = new SSHClient();
						ssh2.addHostKeyVerifier(new PromiscuousVerifier());
						Display.FRAME.getContentPane().add(new JLabel("Reconnecting to " + ip + ":" + port + "..."));
						System.out.println("Reconnecting to " + ip + ":" + port + "...");
						Display.refresh();
						ssh2.connect(ip, port);
						Display.FRAME.getContentPane().add(new JLabel("Logging in as user 'root'..."));
						System.out.println("Logging in as user 'root'...");
						Display.refresh();
						ssh2.authPassword("root", rootPass);
						Session session2 = ssh2.startSession();
						JOptionPane.showMessageDialog(null, "Please make sure your device is unlocked and on the home screen.\nBe prepared to authenticate with your fingerprint or face.");
						Display.FRAME.getContentPane().add(new JLabel("Dumping your device's Keychain... (authenticate with Touch ID or Face ID if asked)"));
						System.out.println("Dumping your device's Keychain... (if this blocks, make sure your device is unlocked)");
						Display.refresh();
						Session.Command cmd = session2.exec("./../mobile/Documents/keychain_dumper");
						keychain = IOUtils.readFully(cmd.getInputStream()).toString();
						session2 = ssh2.startSession();
						Display.FRAME.getContentPane().add(new JLabel("Removing keychain_dumper from device..."));
						System.out.println("Removing keychain_dumper from device...");
						Display.refresh();
						session2.exec("rm /User/Documents/keychain_dumper");
						session2 = ssh2.startSession();
						Display.FRAME.getContentPane().add(new JLabel("Removing ent.xml from device..."));
						System.out.println("Removing ent.xml from device...");
						Display.refresh();
						session2.exec("rm /User/Documents/ent.xml");
						Display.FRAME.getContentPane().add(new JLabel("Disconnecting..."));
						System.out.println("Disconnecting...");
						Display.refresh();
						ssh2.disconnect();
						ssh2.close();
						Display.FRAME.getContentPane().add(new JLabel("Parsing Keychain dump..."));
						System.out.println("Parsing Keychain dump...");
						Display.refresh();
						String[] list = keychain.split("ParentalControls")[1].split("\n");
						String password = null;
						for (int i = 0; i < (list.length > 20 ? 20 : list.length); i++) {
							if (list[i].contains("Keychain Data: ")) {
								password = list[i].split("Keychain Data: ")[1];
								break;
							}
						}
						Display.FRAME.getContentPane().add(new JLabel("Found Screen Time passcode! Passcode: " + password));
						System.out.println("Found Screen Time passcode! Passcode: " + password);
						Display.refresh();
						JButton keychainOutput = new JButton("View Keychain dump");
						keychainOutput.addActionListener(new KeychainOutputListener(keychain));
						JPanel panel = new JPanel();
						panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
						//panel.add(keychainOutput);
						JButton button = new JButton("Back");
						button.addActionListener(new BackListener());
						panel.add(button);
						Display.FRAME.getContentPane().add(panel);
						Display.refresh();
						JOptionPane.showMessageDialog(null, "Found Screen Time passcode! Passcode: " + password);
					} catch (Exception ex) {
						Display.FRAME.getContentPane().removeAll();
						Display.FRAME.getContentPane().add(new JLabel("Failed to retrieve Screen Time passcode!"));
						Display.FRAME.getContentPane().add(new JLabel("If you're sure you've done everything correctly, create an issue on GitHub."));
						Display.FRAME.getContentPane().add(new JLabel(ex.getClass().toString().split(" ")[1]+ ": " + ex.getMessage()));
						JPanel panel = new JPanel();
						panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
						JButton button = new JButton("Back");
						button.addActionListener(new BackListener());
						if (!(keychain == null)) {
							JButton keychainView = new JButton("View failed Keychain dump");
							keychainView.addActionListener(new KeychainOutputListener(keychain));
							//panel.add(keychainView);
						}
						panel.add(button);
						Display.FRAME.getContentPane().add(panel);
						Display.refresh();
						Display.handleException(ex, true);
					}
				}
			}.start();
		}
	}
	public class KeychainOutputListener implements ActionListener {
		private String dump;
		public KeychainOutputListener(String dump) {
			this.dump = dump;
		}
		public void actionPerformed(ActionEvent ev) {
			try {
				PrintWriter writer = new PrintWriter("keychainoutput.txt");
				writer.println(dump);
				writer.flush();
				writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			Display.FRAME.getContentPane().removeAll();
			dump = "<html><body>" + dump + "</body></html>";
			String[] dumpArr = dump.split("\n");
			dump = "";
			for (int i = 0; i < dumpArr.length; i++) {
				dump += dumpArr[i] + "<br/>";
			}
			Display.FRAME.getContentPane().add(new JLabel("<html><body>Keychain dump:<br/></body></html>"));
			Display.FRAME.getContentPane().add(new JLabel(dump));
			JButton back = new JButton("Back to main menu");
			back.addActionListener(new BackListener());
			Display.FRAME.getContentPane().add(back);
			Display.refresh();
		}
	}
	public class ItunesBackupListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				OperatingSystemType currentOS = RestrictionsRecovery.identifyHostOS();
				String backupPath = System.getProperty("user.home") + "\\";
				if (currentOS == OperatingSystemType.WINDOWS) {
					File uwpiTunes = new File(backupPath + "AppData\\Local\\Microsoft\\WindowsApps\\AppleInc.iTunes_nzyj5cx40ttqa");
					if (uwpiTunes.exists()) {
						backupPath += "Apple\\MobileSync\\Backup\\";
					} else {
						backupPath += "AppData\\Roaming\\Apple Computer\\MobileSync\\Backup\\";
					}
				} else if (currentOS == OperatingSystemType.MACOS_MOJAVE_OR_OLDER || currentOS == OperatingSystemType.MACOS_CATALINA_OR_NEWER) {
					throw new Exception("macOS is not currently supported. Support will come in a future update.");
				} else {
					throw new Exception("Your OS is not supported because it is not possible to install iTunes on your OS.");
				}
				File backupLocation = new File(backupPath);
				String[] backups = backupLocation.list(new FilenameFilter() {
					@Override
					public boolean accept(File current, String name) {
						return new File(current, name).isDirectory();
					}
				});
				Display.FRAME.getContentPane().removeAll();
				Display.FRAME.getContentPane().add(new JLabel(RRConst.ITUNES_BACKUPS));
				plists = new InfoPlist[backups.length];
				for (int i = 0; i < backups.length; i++) {
					InfoPlist plist = InfoPlist.getInstance(backupPath + backups[i] + "\\");
					plists[i] = plist;
					if (plist != null) {
						JButton button = new JButton(plist.getModelName() + " / " + plist.getiOSVersion() + " / " + plist.getBackupDate() + " / " + plist.getDisplayName() + " / " + plist.getDeviceName());
						if (plist.getiOSRelease() < 7 || plist.getiOSRelease() > 11) {
							button.setEnabled(false);
						} else if (!new File(plist.getFile() + "398bc9c2aeeab4cb0c12ada0f52eea12cf14f40b").exists()) {
							button.setEnabled(false);
						}
						button.addActionListener(Display.CLASS_OBJ.new BackupListener(i));
						Display.FRAME.getContentPane().add(button);
					}
				}
				JButton back = new JButton("Back");
				back.addActionListener(new BackListener());
				Display.FRAME.getContentPane().add(back);
				Display.refresh();
			} catch (Exception ex) {
				if (ex instanceof IndexOutOfBoundsException) {
					Display.handleException(new Exception("Screen Time passcode could not be found in dump. Create an issue on GitHub that includes a summary of what you see in your Keychain dump if you're sure you've done everything correctly."), true);
					Display.handleException(new Exception(""), true);
				}
				Display.handleException(ex, true);
			}
		}
	}
	public class BackupListener implements ActionListener {
		private int index;
		public BackupListener(int index) {
			this.index = index;
		}
		public void actionPerformed(ActionEvent ev) {
			try {
				KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist(plists[index].getFile() + "398bc9c2aeeab4cb0c12ada0f52eea12cf14f40b");
				JLabel label = new JLabel("<html><body><strong>Calculating passcode...</strong></body></html>");
				Display.FRAME.getContentPane().add(label);
				Display.refresh();
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							String passcode = RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), false);
							if (passcode == null) {
								throw new Exception("Passcode could not be found. Key and salt does not correspond to any passcode between 0000 and 9999.");
							} else {
								JOptionPane.showMessageDialog(null, "Passcode: " + passcode, "Passcode found!", 1);
							}
							Display.FRAME.getContentPane().remove(label);
							Display.refresh();
						} catch (Exception ex) {
							Display.handleException(ex, true);
						}
					}
				};
				thread.start();
			} catch (Exception ex) {
				Display.handleException(ex, true);
			}
		}
	}
	public static void refresh() {
		Display.FRAME.revalidate();
		Display.FRAME.repaint();
	}
	public static void refreshThread() {
		new Thread() {
			@Override
			public void run() {
				Display.refresh();
			}
		}.start();
	}
	public static class BackListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			Display.FRAME.getContentPane().removeAll();
			Display.createDisplay();
			Display.refresh();
		}
	}
	public class KeySaltButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				String key = JOptionPane.showInputDialog("Key?");
				String salt = JOptionPane.showInputDialog("Salt?");
				Display.DESC.setText("<html><b>Calculating passcode...</b></html>");
				Display.refresh();
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							String passcode = RestrictionsRecovery.calculate(key, salt, false);
							if (passcode == null) {
								throw new Exception("Passcode could not be found. Key and salt does not correspond to any passcode between 0000 and 9999.");
							} else {
								JOptionPane.showMessageDialog(null, "Passcode: " + passcode, "Passcode found!", 1);
							}
						} catch (Exception ex) {
							Display.handleException(ex, true);
						}
					}
				};
				thread.start();
			} catch (Exception ex) {
				Display.handleException(ex, true);
			}
			Display.DESC.setText(RRConst.DESC);
			Display.refresh();
		}
	}
	public class FileButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				String file = JOptionPane.showInputDialog("Property list path? (Do not include quotes)");
				KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist(file);
				Display.DESC.setText("<html><b>Calculating passcode...</b></html>");
				Display.refresh();
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							String passcode = RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), false);
							if (passcode == null) {
								throw new Exception("Passcode could not be found. Key and salt does not correspond to any passcode between 0000 and 9999.");
							} else {
								JOptionPane.showMessageDialog(null, "Passcode: " + passcode, "Passcode found!", 1);
							}
						} catch (Exception ex) {
							Display.handleException(ex, true);
						}
					}
				};
				thread.start();
			} catch (Exception ex) {
				Display.handleException(ex, true);
			}
			Display.DESC.setText(RRConst.DESC);
			Display.refresh();
		}
	}
	public class SSHButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				String ip = JOptionPane.showInputDialog("Device IP address?");
				String password = JOptionPane.showInputDialog("Device root password?");
				String port = JOptionPane.showInputDialog("Device SSH port?");
				Display.DESC.setText("<html><b>Calculating passcode...</b></html>");
				Display.refresh();
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							Display.DESC.setText("Downloading passcode from device...");
							RestrictionsRecovery.downloadViaSSH(ip, Integer.parseInt(port), password, false);
							Display.DESC.setText("Bruteforcing passcode...");
							KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist("password.plist");
							String passcode = RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), false);
							if (passcode == null) {
								throw new Exception("Passcode could not be found. Specified key and salt do not correspond to any passcode between 0000 and 9999.");
							} else {
								JOptionPane.showMessageDialog(null, "Passcode: " + passcode, "Passcode found!", 1);
							}
						} catch (Exception ex) {
							Display.handleException(ex, true);
						}
					}
				};
				thread.start();
			} catch (Exception ex) {
				Display.handleException(ex, true);
			}
			Display.DESC.setText(RRConst.DESC);
			Display.refresh();
		}
	}
	public class IproxyButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			// not implemented yet
		}
	}
	private static void handleException(Exception ex, boolean message) {
		ex.printStackTrace();
		if (message) {
			JOptionPane.showMessageDialog(null, "Error: " + ex.getClass().getName() + ": " + ex.getMessage(), "An exception has occurred", 0);
		}
	}
}