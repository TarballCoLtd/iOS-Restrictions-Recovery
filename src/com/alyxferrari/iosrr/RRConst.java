package com.alyxferrari.iosrr;
public class RRConst {
	private RRConst() {}
	public static final String NAME = "iOS-Restrictions-Recovery";
	public static final String VERSION = "v1.0 beta 5";
	public static final String AUTHOR = "Alyx Ferrari";
	public static final String FULL_NAME = NAME + " " + VERSION;
	public static final String TITLE = "<html><body><font size=\"5\">" + FULL_NAME + "</font></body></html>";
	public static final String DESC = "<html><body>Compatible with iOS 7.0 through iOS 14.8<br/><br/></body></html>";
	public static final String KEY_SALT_BUTTON = "From key and salt";
	public static final String FILE_BUTTON = "From property list (plist) file";
	public static final String SSH_BUTTON = "From device via SSH";
	public static final String IPROXY_BUTTON = "From device via iproxy over USB";
	public static final String ITUNES_BACKUP = "From unencrypted iTunes backup";
	public static final String ITUNES_BACKUPS = "iTunes Backups:";
	public static final String ITUNES_BACKUP_12 = "From encrypted iTunes backup (iOS 12 only)";
	public static final String KEYCHAIN_DUMPER = "From device via SSH";
	public static final String ABOUT = "About/Credits";
	public static final String iOS_13 = "<html><body><br/>iOS 12 through iOS 14:</body></html>";
	public static final String iOS_11 = "<html><body><br/>iOS 7 through iOS 11:</body></html>";
}