package com.emeryferrari.iosrr;
import javax.xml.parsers.*;
import org.w3c.dom.*;
public class InfoPlist {
	private final String deviceName;
	private final String displayName;
	private final String backupDate;
	private final String iOSVersion;
	private final String file;
	private InfoPlist(String deviceName, String displayName, String backupDate, String iOSVersion, String file) {
		this.deviceName = deviceName;
		this.displayName = displayName;
		this.backupDate = backupDate;
		this.iOSVersion = iOSVersion;
		this.file = file;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public String getBackupDate() {
		return backupDate;
	}
	public String getiOSVersion() {
		return iOSVersion;
	}
	public String getFile() {
		return file;
	}
	public int getiOSRelease() {
		String version = iOSVersion.split(" ")[1].substring(0, 2);
		if (version.endsWith(".")) {
			return Integer.parseInt(version.substring(0, 1));
		}
		return Integer.parseInt(version);
	}
	public static InfoPlist getInstance(String file) {
		try {
			String fileTemp = file + "Info.plist";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fileTemp);
			document.getDocumentElement().normalize();
			NodeList nodes = document.getElementsByTagName("string");
			String displayName = nodes.item(1).getTextContent();
			boolean stop = false;
			String deviceName = null;
			String iOSVersion = null;
			for (int i = 4; !stop; i++) {
				if (nodes.item(i).getTextContent().startsWith("iP")) {
					deviceName = nodes.item(i+1).getTextContent();
					iOSVersion = "iOS " + nodes.item(i+2).getTextContent();
					stop = true;
				}
			}
			String backupDate = document.getElementsByTagName("date").item(0).getTextContent();
			String backupDate1 = backupDate.substring(0, 10);
			String backupDate2 = backupDate.substring(14, 19);
			backupDate = backupDate1 + "@" + backupDate2;
			return new InfoPlist(deviceName, displayName, backupDate, iOSVersion, file);
		} catch (Exception ex) {
			return null;
		}
	}
}