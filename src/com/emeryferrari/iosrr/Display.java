package com.emeryferrari.iosrr;
import javax.swing.*;
import java.awt.event.*;
public class Display {
	private Display() {}
	private static final Display CLASS_OBJ = new Display();
	private static JFrame FRAME = new JFrame(RRConst.NAME + " - v" + RRConst.VERSION);
	private static JLabel TITLE = new JLabel(RRConst.TITLE);
	private static JButton KEY_SALT_BUTTON = new JButton(RRConst.KEY_SALT_BUTTON);
	private static JButton FILE_BUTTON = new JButton(RRConst.FILE_BUTTON);
	private static JButton SSH_BUTTON = new JButton(RRConst.SSH_BUTTON);
	private static JButton IPROXY_BUTTON = new JButton(RRConst.IPROXY_BUTTON);
	public static void createDisplay() {
		Display.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Display.FRAME.setSize(800, 600);
		Display.FRAME.setLayout(new BoxLayout(FRAME.getContentPane(), BoxLayout.Y_AXIS));
		Display.KEY_SALT_BUTTON.addActionListener(Display.CLASS_OBJ.new KeySaltButtonListener());
		Display.FILE_BUTTON.addActionListener(Display.CLASS_OBJ.new FileButtonListener());
		Display.SSH_BUTTON.addActionListener(Display.CLASS_OBJ.new SSHButtonListener());
		Display.IPROXY_BUTTON.addActionListener(Display.CLASS_OBJ.new IproxyButtonListener());
		Display.IPROXY_BUTTON.setEnabled(false);
		Display.FRAME.getContentPane().add(Display.TITLE);
		Display.FRAME.getContentPane().add(Display.KEY_SALT_BUTTON);
		Display.FRAME.getContentPane().add(Display.FILE_BUTTON);
		Display.FRAME.getContentPane().add(Display.SSH_BUTTON);
		Display.FRAME.getContentPane().add(Display.IPROXY_BUTTON);
		Display.FRAME.setVisible(true);
	}
	public class KeySaltButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				String key = JOptionPane.showInputDialog("Key?");
				String salt = JOptionPane.showInputDialog("Salt?");
				String passcode = RestrictionsRecovery.calculate(key, salt, false);
				if (passcode == null) {
					throw new Exception();
				} else {
					JOptionPane.showMessageDialog(null, "Passcode: " + passcode);
				}
			} catch (Exception ex) {
				Display.handleException(ex);
			}
		}
	}
	public class FileButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				String file = JOptionPane.showInputDialog("Property list path?");
				KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist(file);
				String passcode = RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), false);
				if (passcode == null) {
					throw new Exception();
				} else {
					JOptionPane.showMessageDialog(null, "Passcode: " + passcode);
				}
			} catch (Exception ex) {
				Display.handleException(ex);
			}
		}
	}
	public class SSHButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				String ip = JOptionPane.showInputDialog("Device IP address?");
				String password = JOptionPane.showInputDialog("Device root password?");
				String port = JOptionPane.showInputDialog("Device SSH port?");
				RestrictionsRecovery.downloadViaSSH(ip, Integer.parseInt(port), password, false);
				KeySaltPair pair = PropertyListReader.getKeyAndSaltFromPlist("password.plist");
				String passcode = RestrictionsRecovery.calculate(pair.getKey(), pair.getSalt(), false);
				if (passcode == null) {
					throw new Exception();
				} else {
					JOptionPane.showMessageDialog(null, "Passcode: " + passcode);
				}
			} catch (Exception ex) {
				Display.handleException(ex);
			}
		}
	}
	public class IproxyButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			// not implemented yet
		}
	}
	private static void handleException(Exception ex) {
		ex.printStackTrace();
		JOptionPane.showMessageDialog(null, "Error occurred while calculating the passcode. Check the console for details.");
	}
}