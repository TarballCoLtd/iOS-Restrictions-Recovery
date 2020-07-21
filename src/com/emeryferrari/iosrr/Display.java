package com.emeryferrari.iosrr;
import javax.swing.*;
import java.net.*;
import java.awt.event.*;
import java.io.*;
import net.schmizz.sshj.*;
import net.schmizz.sshj.common.*;
import net.schmizz.sshj.transport.verification.*;
import net.schmizz.sshj.connection.channel.direct.*;
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
			Display.FRAME.setSize(550, 400);
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
			JLabel slf4j = new JLabel("slf4j Copyright (c) 2004-2017 QOS.ch");
			JLabel bcJava = new JLabel("bc-java Copyright (c) 2000-2019 The Legion of the Bouncy Castle Inc.");
			JLabel sshj = new JLabel("sshj Copyright (c) 2010-2012 sshj contributors");
			JLabel keychainDumper = new JLabel("keychain_dumper was written by ptoomey3 on GitHub");
			JLabel backup = new JLabel("The iTunes backup idea was given to me by Reddit users u/Starwarsfan2099 and u/KuroAMK");
			JLabel affiliation = new JLabel("This program is in no way affiliated with Apple, Inc.");
			JButton back = new JButton("Back");
			back.addActionListener(new BackListener());
			Display.FRAME.getContentPane().add(title);
			Display.FRAME.getContentPane().add(author);
			Display.FRAME.getContentPane().add(slf4j);
			Display.FRAME.getContentPane().add(bcJava);
			Display.FRAME.getContentPane().add(sshj);
			Display.FRAME.getContentPane().add(keychainDumper);
			Display.FRAME.getContentPane().add(backup);
			Display.FRAME.getContentPane().add(affiliation);
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
					try {
						String ip = JOptionPane.showInputDialog("Device IP address? OpenSSH and SQLite 3.x must be installed on your device.");
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
						if (!keychain_dumper.exists()) {
							Display.FRAME.getContentPane().add(new JLabel("Couldn't find keychain_dumper!"));
							JLabel progress = new JLabel("Downloading keychain_dumper from GitHub...");
							Display.FRAME.getContentPane().add(progress);
							Display.refresh();
							URL url = new URL("https://raw.githubusercontent.com/ptoomey3/Keychain-Dumper/master/keychain_dumper");
							InputStream is = url.openStream();
							FileOutputStream fos = new FileOutputStream("keychain_dumper");
							URLConnection connection = url.openConnection();
							if (connection instanceof HttpURLConnection) {
								((HttpURLConnection)connection).setRequestMethod("HEAD");
							}
							connection.getInputStream();
							int total = connection.getContentLength();
							int b;
							byte[] data = new byte[1024];
							int count = 0;
							while ((b = is.read(data, 0, 1024)) != -1) {
								fos.write(data, 0, b);
								count += b;
								progress.setText("Downloading keychain_dumper from GitHub... (" + count + " bytes / " + total + " bytes)");
								Display.refresh();
							}
							fos.flush();
							fos.close();
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
						Session session = ssh.startSession();
						Display.FRAME.getContentPane().add(new JLabel("Giving keychain_dumper '+x' permissions..."));
						System.out.println("Giving keychain_dumper '+x' permissions...");
						Display.refresh();
						session.exec("chmod +x /User/Documents/keychain_dumper");
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
						JOptionPane.showMessageDialog(null, "Please make sure your device is unlocked and on the home screen.");
						Display.FRAME.getContentPane().add(new JLabel("Dumping your device's Keychain... (if this blocks, make sure your device is unlocked)"));
						System.out.println("Dumping your device's Keychain... (if this blocks, make sure your device is unlocked)");
						Display.refresh();
						Session.Command cmd = session2.exec("./../mobile/Documents/keychain_dumper");
						String keychain = IOUtils.readFully(cmd.getInputStream()).toString();
						session2 = ssh2.startSession();
						Display.FRAME.getContentPane().add(new JLabel("Removing keychain_dumper from device..."));
						System.out.println("Removing keychain_dumper from device...");
						Display.refresh();
						session2.exec("rm ./../mobile/Documents/keychain_dumper");
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
						for (int i = 0; i < 20; i++) {
							if (list[i].startsWith("Keychain Data: ")) {
								password = list[i].split(": ")[1];
								break;
							}
						}
						Display.FRAME.getContentPane().add(new JLabel("Found Screen Time passcode! Passcode: " + password));
						System.out.println("Found Screen Time passcode! Passcode: " + password);
						Display.refresh();
						JButton button = new JButton("Back");
						button.addActionListener(new BackListener());
						Display.FRAME.getContentPane().add(button);
						Display.refresh();
						JOptionPane.showMessageDialog(null, "Found Screen Time passcode! Passcode: " + password);
					} catch (Exception ex) {
						handleException(ex, true);
						Display.FRAME.getContentPane().add(new JLabel("Failed to retrieve Screen Time passcode! If you're sure you've done everything correctly, create an issue on GitHub."));
						Display.FRAME.getContentPane().add(new JLabel(ex.getClass().toString().split(" ")[1]+ ": " + ex.getMessage()));
						JButton button = new JButton("Back");
						button.addActionListener(new BackListener());
						Display.FRAME.getContentPane().add(button);
						Display.refresh();
					}
				}
			}.start();
		}
	}
	public class ItunesBackupListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				OperatingSystem currentOS = RestrictionsRecovery.identifyHostOS();
				String backupPath = System.getProperty("user.home") + "\\";
				if (currentOS == OperatingSystem.WINDOWS) {
					String y = "n";
					File uwpiTunes = new File(backupPath + "AppData\\Local\\Microsoft\\WindowsApps\\AppleInc.iTunes_nzyj5cx40ttqa");
					if (uwpiTunes.exists()) {
						y = "y";
					}
					if (y.equalsIgnoreCase("y") || y.equalsIgnoreCase("yes")) {
						backupPath += "Apple\\MobileSync\\Backup\\";
					} else {
						backupPath += "AppData\\Roaming\\Apple Computer\\MobileSync\\Backup\\";
					}
				} else if (currentOS == OperatingSystem.MACOSMOJAVE || currentOS == OperatingSystem.MACOSCATALINA) {
					
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
			JOptionPane.showMessageDialog(null, "Error: " + ex.getClass().toString().split(" ")[1] + ": " + ex.getMessage(), "An exception has occurred", 0);
		}
	}
}