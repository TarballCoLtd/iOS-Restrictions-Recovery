package com.emeryferrari.iosrr;
public class RRConst {
	private RRConst() {}
	public static final String NAME = "iOS-Restrictions-Recovery";
	public static final String VERSION = "v0.7.1";
	public static final String FULL_NAME = NAME + " " + VERSION;
	public static final String TITLE = "<html><body><font size=\"6\">" + FULL_NAME + "</font></body></html>";
	public static final String DESC = "<html><body>Compatible <strong>only</strong> with <strong>iOS 7.0</strong> through <strong>iOS 13.x</strong></body></html>";
	public static final String KEY_SALT_BUTTON = "From key and salt";
	public static final String FILE_BUTTON = "From property list file";
	public static final String SSH_BUTTON = "From device via SSH (iOS 7.0 through iOS 11.4.1)";
	public static final String IPROXY_BUTTON = "From device via iproxy over USB";
	public static final String ITUNES_BACKUP = "From unencrypted iTunes backup";
	public static final String ITUNES_BACKUPS = "iTunes Backups:";
	public static final String ITUNES_BACKUP_12 = "From encrypted iTunes backup (iOS 12 only)";
	public static final String KEYCHAIN_DUMPER = "From device via SSH (iOS 12.0 through iOS 13.x)";
	public static final String ABOUT = "About/Credits";
}