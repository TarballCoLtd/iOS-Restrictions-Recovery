package com.emeryferrari.iosrr;
public class RRArguments {
	private final RequestType type;
	private boolean iproxy;
	private String ssh;
	String password;
	String port;
	String file;
	String key;
	String salt;
	boolean version;
	public RRArguments(boolean iproxy, String ssh, String password, String port, String file, String key, String salt, boolean version) {
		this.iproxy = false;
		this.ssh = null;
		this.password = null;
		this.port = null;
		this.file = null;
		this.key = null;
		this.salt = null;
		this.version = false;
		if (iproxy && ssh == null && file == null && key == null && salt == null && !version) {
			this.iproxy = true;
			if (password != null) {
				this.password = password;
			}
			if (port != null) {
				this.port = port;
			}
			type = RequestType.IPROXY;
		} else if (!iproxy && ssh != null && file == null && key == null && salt == null && !version) {
			this.ssh = ssh;
			if (password != null) {
				this.password = password;
			}
			if (port != null) {
				this.port = port;
			}
			type = RequestType.SSH;
		} else if (!iproxy && ssh == null && file != null && key == null && salt == null && !version) {
			this.file = file;
			type = RequestType.FILE;
		} else if (!iproxy && ssh == null && file == null && key != null && salt != null && !version) {
			this.key = key;
			this.salt = salt;
			type = RequestType.KEYSALT;
		} else if (!iproxy && ssh == null && file == null && key == null && salt == null && version) {
			type = RequestType.VERSION;
		} else {
			type = RequestType.NONSENSICAL;
		}
	}
	public RequestType getRequestType() {
		return type;
	}
	public boolean getIproxy() {
		return iproxy;
	}
	public String getSsh() {
		return ssh;
	}
	public String getPassword() {
		return password;
	}
	public String getPort() {
		return port;
	}
	public String getFile() {
		return file;
	}
	public String getKey() {
		return key;
	}
	public String getSalt() {
		return salt;
	}
	public boolean getVersion() {
		return version;
	}
}