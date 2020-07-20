package com.emeryferrari.iosrr;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
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
	public String getModelName() {
		return identifierToName(deviceName);
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
		if (getiOSRelease() == -1) {
			return "iOS version couldn't be read";
		} else {
			return iOSVersion;
		}
	}
	public String getFile() {
		return file;
	}
	public int getiOSRelease() {
		try {
			String version = iOSVersion.split(" ")[1].substring(0, 2);
			if (version.endsWith(".")) {
				return Integer.parseInt(version.substring(0, 1));
			}
			return Integer.parseInt(version);
		} catch (NumberFormatException ex) {
			return -1;
		}
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
			for (int i = 3; !stop; i++) {
				if (nodes.item(i).getTextContent().startsWith("iP")) {
					FileInputStream fis = new FileInputStream(fileTemp);
					BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
					String result = "";
					String line = "";
					while ((line = reader.readLine()) != null) {
						result += line;
					}
					reader.close();
					if (result.contains("Product Name")) {
						deviceName = nodes.item(i+1).getTextContent();
						iOSVersion = "iOS " + nodes.item(i+2).getTextContent();
					} else {
						deviceName = nodes.item(i).getTextContent();
						iOSVersion = "iOS " + nodes.item(i+1).getTextContent();
					}
					stop = true;
				}
			}
			String backupDate = document.getElementsByTagName("date").item(0).getTextContent();
			String backupDate1 = backupDate.substring(0, 10);
			String backupDate2 = backupDate.substring(14, 19);
			backupDate = backupDate1 + "@" + backupDate2;
			return new InfoPlist(deviceName, displayName, backupDate, iOSVersion, file);
		} catch (Exception ex) {}
		return null;
	}
	public static String identifierToName(String identifier) {
		if (identifier.equals("iPhone1,1")) {
			return "iPhone 2G";
		} else if (identifier.equals("iPhone1,2")) {
			return "iPhone 3G";
		} else if (identifier.equals("iPhone2,1")) {
			return "iPhone 3Gs";
		} else if (identifier.equals("iPhone3,1")) {
			return "iPhone 4";
		} else if (identifier.equals("iPhone3,2")) {
			return "iPhone 4 (GSM)";
		} else if (identifier.equals("iPhone3,3")) {
			return "iPhone 4 (CDMA)";
		} else if (identifier.equals("iPhone4,1")) {
			return "iPhone 4s";
		} else if (identifier.equals("iPhone5,1")) {
			return "iPhone 5 (GSM)";
		} else if (identifier.equals("iPhone5,2")) {
			return "iPhone 5 (GSM/CDMA)";
		} else if (identifier.equals("iPhone5,3")) {
			return "iPhone 5c (GSM)";
		} else if (identifier.equals("iPhone5,4")) {
			return "iPhone 5c (Global)";
		} else if (identifier.equals("iPhone6,1")) {
			return "iPhone 5s (GSM)";
		} else if (identifier.equals("iPhone6,2")) {
			return "iPhone 5s (Global)";
		} else if (identifier.equals("iPhone7,1")) {
			return "iPhone 6 Plus";
		} else if (identifier.equals("iPhone7,2")) {
			return "iPhone 6";
		} else if (identifier.equals("iPhone8,1")) {
			return "iPhone 6s";
		} else if (identifier.equals("iPhone8,2")) {
			return "iPhone 6s Plus";
		} else if (identifier.equals("iPhone8,4")) {
			return "iPhone SE (1st generation)";
		} else if (identifier.equals("iPhone9,1") || identifier.equals("iPhone9,3")) {
			return "iPhone 7";
		} else if (identifier.equals("iPhone9,2") || identifier.equals("iPhone9,4")) {
			return "iPhone 7 Plus";
		} else if (identifier.equals("iPhone10,1") || identifier.equals("iPhone10,4")) {
			return "iPhone 8";
		} else if (identifier.equals("iPhone10,2") || identifier.equals("iPhone10,5")) {
			return "iPhone 8 Plus";
		} else if (identifier.equals("iPhone10,3")) {
			return "iPhone X (Global)";
		} else if (identifier.equals("iPhone10,6")) {
			return "iPhone X (GSM)";
		} else if (identifier.equals("iPhone11,2")) {
			return "iPhone Xs";
		} else if (identifier.equals("iPhone11,4")) {
			return "iPhone Xs Max";
		} else if (identifier.equals("iPhone11,6")) {
			return "iPhone Xs Max (Global)";
		} else if (identifier.equals("iPhone11,8")) {
			return "iPhone XR";
		} else if (identifier.equals("iPhone12,1")) {
			return "iPhone 11";
		} else if (identifier.equals("iPhone12,3")) {
			return "iPhone 11 Pro";
		} else if (identifier.equals("iPhone12,5")) {
			return "iPhone 11 Pro Max";
		} else if (identifier.equals("iPhone12,8")) {
			return "iPhone SE (2nd generation)";
		} else if (identifier.equals("iPod1,1")) {
			return "iPod touch 1G";
		} else if (identifier.equals("iPod2,1")) {
			return "iPod touch 2G";
		} else if (identifier.equals("iPod3,1")) {
			return "iPod touch 3G";
		} else if (identifier.equals("iPod4,1")) {
			return "iPod touch 4G";
		} else if (identifier.equals("iPod5,1")) {
			return "iPod touch 5G";
		} else if (identifier.equals("iPod7,1")) {
			return "iPod touch 6G";
		} else if (identifier.equals("iPod9,1")) {
			return "iPod touch 7G";
 		} else if (identifier.startsWith("iPad")) {
 			return "iPad";
 		}
		return identifier;
	}
}