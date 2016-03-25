## Processing MethCla Interface

This is a processing interface and a collection of plugins for MethCla, a leight-weight, efficient sound engine for mobile devices [methcla](http://methc.la). For the moment this library is OSX + Linux only.


## Building the libMethClaInterface

The Java Library is to be compiled with ant. Please install the latest version on ant on your computer. Clone this repository as 'sound' to sketchbook/libraries. The build.xml file is in in the root folder. Edit the properties in build.properties file to add the path of the core library of processing. Core.jar needs to be compiled and ready in ../../../core/library. To compile do

 ```bash
  $ ant
 ```

in the root folder.
