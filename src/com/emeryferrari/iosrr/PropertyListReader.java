package com.emeryferrari.iosrr;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
public class PropertyListReader {
	private PropertyListReader() {}
	public static KeySaltPair getKeyAndSaltFromPlist(String plist) throws ParserConfigurationException, IOException, SAXException {
		File plistFile = new File(plist);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(plistFile);
		document.getDocumentElement().normalize();
		String key = document.getElementsByTagName("data").item(0).getTextContent().split("	")[1].split("\n")[0];
		String salt = document.getElementsByTagName("data").item(1).getTextContent().split("	")[1].split("\n")[0];
		return new KeySaltPair(key, salt);
	}
}