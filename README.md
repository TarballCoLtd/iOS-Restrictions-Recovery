# iOS-Restrictions-Recovery

by Tarball<br/>
A GUI/CLI tool that can find the Restrictions or Screen Time passcode of any iOS device running iOS 7.0 through iOS 12.4.9. iOS 13 and 14 *should* work, but Keychain-Dumper doesn't work consistently on these versions.

## Credit

Special thanks to [ptoomey3](https://github.com/ptoomey3/) for [Keychain-Dumper](https://github.com/ptoomey3/Keychain-Dumper/).<br/><br/>
The code for the iTunes backup feature was loosely based on [this GitHub project](https://github.com/Starwarsfan2099/iOS-Restriction-Key-Cracker) by [u/Starwarsfan2099](https://reddit.com/user/Starwarsfan2099)

## Dependencies

All dependencies are handled by Maven.<br/>
sshj<br/>
slf4j<br/>
ed25519<br/>
bcprov-jdk15on<br/>
bcpkix-jdk15on<br/>
jzlib

## Compilation/Execution

To run this program, you can either download the JAR from the Releases tab or generate one yourself with Maven.<br/>
OpenSSH is required to use all iOS 12-15 features and the iOS 7.0-11.4.1 SSH features. If you're using checkra1n, iproxy from libimobiledevice will work as an alternative. If you're using any other jailbreak, OpenSSH is available on the default repos.<br/>
The SQLite 3.x package is also required for the iOS 12-15 tools.
