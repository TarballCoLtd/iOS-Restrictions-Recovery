# iOS Restrictions Recovery

by Emery Ferrari<br/>
A command-line tool (for now) that will recover the restrictions passcode from a device running iOS 7.0-11.4.1.

## Credit

slf4j Copyright (c) 2004-2017 QOS.ch
bc-java Copyright (c) 2000-2019 The Legion of the Bouncy Castle Inc.
sshj Copyright (c) 2010-2012 sshj contributors

## Dependencies

To compile:<br/>
slf4j (slf4j-api-1.7.2 and slf4j-jdk14-1.7.2 are used for compilation of the release jars)
sshj (sshj-0.27.0 is used for compilation of the release jars)
ed25519-java (eddsa-0.3.0 is used for compilation of the release jars)
bc-java (bcprov-jdk15on-1.64 is used for compilation of the release jars)

## Compilation/Execution

This tool can either be run from the .jar executable of the latest release in the Releases tab, or can be compiled using javac.

## Prerequisites

Java Runtime Environment or Java Development Kit<br/>
Windows OS

## Command-line arguments

-ssh ip_address, --secure-shell ip_address: connects to a jailbroken device and recovers the restrictions passcode directly from it; must have OpenSSH installed on the target device<br/>
-password password: (optional) specifies SSH password when connecting to device via SSH, if no password is specified, 'alpine' will be used<br/>
-port port: (optional) specifies SSH port when connecting to device via SSH, if no port is specified, 22 will be used<br/>
-f file, -file file: reads the key and salt directly from the passcode property list file<br/>
-k key, -key key: specifies key to use to brute force passcode<br/>
-s salt, -salt salt: specifies salt used to produce key<br/>
-v, -version: displays the version of RestrictionsRecovery installed<br/>
-h, -help: displays this menu

## Known issues

There are many performance and consistency issues in the source code. None of these will affect your experience with the program in practice, however these are planned to be fixed in a future pre-release update nonetheless.

## Contacting me

I will respond to any PM I receive on Reddit.<br/>
[u/verystrangebeing](https://reddit.com/user/verystrangebeing/)