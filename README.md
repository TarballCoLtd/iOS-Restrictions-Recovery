# iOS Restrictions Recovery

by Emery Ferrari<br/>
A command-line tool (for now) that will recover the restrictions passcode from a device running iOS 7.0-11.4.1.

## Credit

slf4j Copyright (c) 2004-2017 QOS.ch<br/>
bc-java Copyright (c) 2000-2019 The Legion of the Bouncy Castle Inc.<br/>
sshj Copyright (c) 2010-2012 sshj contributors

## Dependencies

To compile:<br/>
slf4j (slf4j-api-1.7.2 and slf4j-jdk14-1.7.2 are used for compilation of the release jars)<br/>
sshj (sshj-0.27.0 is used for compilation of the release jars)<br/>
ed25519-java (eddsa-0.3.0 is used for compilation of the release jars)<br/>
bc-java (bcprov-jdk15on-1.64 is used for compilation of the release jars)<br/><br/>
To use the iproxy feature:<br/>
    -macOS: homebrew, libimobiledevice<br/>
    -Unix-based operating systems: libusbmuxd-tools<br/>
    -Windows: support coming soon

## Compilation/Execution

This tool can either be run from the .jar executable of the latest release in the Releases tab, or can be compiled using javac.

## Prerequisites

Java Runtime Environment or Java Development Kit<br/>
macOS, Windows, or a Unix-based operating system

## Known issues

There are many performance and consistency issues in the source code. None of these will affect your experience with the program in practice, however these are planned to be fixed in a future pre-release update nonetheless.

## Contacting me

I will respond to any PM I receive on Reddit.<br/>
[u/verystrangebeing](https://reddit.com/user/verystrangebeing/)