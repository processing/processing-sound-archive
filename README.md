## Processing Methcla Interface

This is a processing interface and a collection of plugins for [Methcla](http://methc.la), a leight-weight, efficient sound engine for mobile devices. For the moment this library is OSX + Linux only.


## Building the libMethClaInterface

The Java Library is to be compiled with ant. Please install the latest version on ant on your computer. The build.xml file is in in the root folder. Core.jar needs to be compiled and ready in ../../../core/library. To compile do

 ```bash
  $ ant
 ```

in the root folder.

## Building on Linux

Install the following packages

    g++
    ant
    openjdk-7-jdk

Point the build scripts to the Java installation

    export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64

### Running the 32 bit version of processing (Ubuntu 14.04)

Execute the following commands:

    sudo dpkg --add-architecture i386
    sudo apt-get update

Install the following packages:

    libc6:i386
    libstdc++6:i386
    libXext6:i386
    libXrender1:i386
    libXtst6:i386
    libXi6:i386
    libpulse0:i386

### Compiling the 32 bit version of Processing Sound (Ubuntu 14.04)

Install the following packages:

    gcc-multilib
    g++-multilib
