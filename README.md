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

### Building on Linux (32 bit)

Install the following packages:

    libasound2-dev:i386
    libsndfile1:i386
    libmpg123-dev:i386

Since `libsndfile1-dev:i386` cannot be installed alongside `libsndfile1-dev`, you need to link the library manually:

    $ cd /usr/lib/i386-linux-gnu
    $ sudo ln -s libsndfile.so.1 libsndfile.so

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

## Building on Windows

You need

* [Haskell stack](http://haskellstack.org)
* [Msys2](https://msys2.github.io/)

And the following packages (install with `pacman -S`):

    mingw-w64-x86_64-toolchain
    mingw-w64-x86_64-libsndfile
    mingw-w64-x86_64-mpg123
    mingw-w64-i686-toolchain
    mingw-w64-i686-libsndfile
    mingw-w64-i686-mpg123

Build the 64-bit version in a **MINGW64** shell with

    $ ant

and the 32-bit version in a **MINGW32** shell with

    $ ant -Darch=32
