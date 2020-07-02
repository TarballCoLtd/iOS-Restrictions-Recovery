package com.emeryferrari.iosrr;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
public class Display {
	private Display() {}
	private static final Display CLASS_OBJ = new Display();
	private static JFrame FRAME = new JFrame(RRConst.NAME + " " + RRConst.VERSION);
	private static JLabel TITLE = new JLabel(RRConst.TITLE);
	private static JLabel DESC = new JLabel(RRConst.DESC);
	private static JButton KEY_SALT_BUTTON = new JButton(RRConst.KEY_SALT_BUTTON);
	private static JButton FILE_BUTTON = new JButton(RRConst.FILE_BUTTON);
	private static JButton SSH_BUTTON = new JButton(RRConst.SSH_BUTTON);
	private static JButton IPROXY_BUTTON = new JButton(RRConst.IPROXY_BUTTON);
	private static JButton ITUNES_BACKUP = new JButton(RRConst.ITUNES_BACKUP);
	private static InfoPlist[] plists = null;
	private static boolean initialized = false;
	public static void createDisplay() {
		if (!initialized) {
			Display.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Display.FRAME.setSize(800, 600);
			Display.FRAME.setLayout(new BoxLayout(FRAME.getContentPane(), BoxLayout.Y_AXIS));
			Display.KEY_SALT_BUTTON.addActionListener(Display.CLASS_OBJ.new KeySaltButtonListener());
			Display.FILE_BUTTON.addActionListener(Display.CLASS_OBJ.new FileButtonListener());
			Display.SSH_BUTTON.addActionListener(Display.CLASS_OBJ.new SSHButtonListener());
			Display.IPROXY_BUTTON.addActionListener(Display.CLASS_OBJ.new IproxyButtonListener());
			Display.IPROXY_BUTTON.setEnabled(false);
			Display.ITUNES_BACKUP.addActionListener(Display.CLASS_OBJ.new ItunesBackupListener());
			initialized = true;
		}
		Display.FRAME.getContentPane().add(Display.TITLE);
		Display.FRAME.getContentPane().add(Display.DESC);
		Display.FRAME.getContentPane().add(Display.KEY_SALT_BUTTON);
		Display.FRAME.getContentPane().add(Display.FILE_BUTTON);
		Display.FRAME.getContentPane().add(Display.SSH_BUTTON);
		Display.FRAME.getContentPane().add(Display.ITUNES_BACKUP);
		Display.FRAME.setVisible(true);
	}
	public class ItunesBackupListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				OperatingSystem currentOS = RestrictionsRecovery.identifyHostOS();
				String backupPath = System.getProperty("user.home") + "\\";
				if (currentOS == OperatingSystem.WINDOWS) {
					String y = JOptionPane.showInputDialog("Are you using the Microsoft Store version of iTunes? (y/n)");
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
						JButton button = new JButton(plist.getDeviceName() + " / " + plist.getiOSVersion() + " / " + plist.getBackupDate() + " / " + plist.getDisplayName());
						if (plist.getiOSRelease() < 7 || plist.getiOSRelease() > 11) {
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
				Display.FRAME.getContentPane().add(new JLabel("<html><body><strong>Calculating passcode...</strong></body></html>"));
				Display.refresh();
				String passcode = RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), false);
				if (passcode == null) {
					throw new Exception("Passcode could not be found. Key and salt does not correspond to any passcode between 0000 and 9999.");
				} else {
					JOptionPane.showMessageDialog(null, "Passcode: " + passcode);
				}
				Display.FRAME.getContentPane().removeAll();
				Display.createDisplay();
				Display.refresh();
			} catch (Exception ex) {
				Display.handleException(ex, true);
			}
		}
	}
	public static void refresh() {
		Display.FRAME.revalidate();
		Display.FRAME.repaint();
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
				String passcode = RestrictionsRecovery.calculate(key, salt, false);
				if (passcode == null) {
					throw new Exception("Passcode could not be found. Key and salt does not correspond to any passcode between 0000 and 9999.");
				} else {
					JOptionPane.showMessageDialog(null, "Passcode: " + passcode);
				}
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
				String passcode = RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), false);
				if (passcode == null) {
					throw new Exception("Passcode could not be found. Key and salt does not correspond to any passcode between 0000 and 9999.");
				} else {
					JOptionPane.showMessageDialog(null, "Passcode: " + passcode);
				}
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
				RestrictionsRecovery.downloadViaSSH(ip, Integer.parseInt(port), password, false);
				KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist("password.plist");
				String passcode = RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), false);
				if (passcode == null) {
					throw new Exception("Passcode could not be found. Key and salt does not correspond to any passcode between 0000 and 9999.");
				} else {
					JOptionPane.showMessageDialog(null, "Passcode: " + passcode);
				}
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
			JOptionPane.showMessageDialog(null, "Error: " + ex.getClass().toString().split(" ")[1] + ": " + ex.getMessage());
		}
	}
}