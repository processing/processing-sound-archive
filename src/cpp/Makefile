all: 	
	g++ -I/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/include/darwin -I./include -std=c++11 -g -c processing_sound_MethClaInterface.cpp;		
	g++ -dynamiclib -lmethcla -L../../library/macosx/ -o libMethClaInterface.jnilib *.o;

clean:
	rm *.o
	rm *.jnilib

install:
	install_name_tool -change @executable_path/libmethcla.dylib  @loader_path/libmethcla.dylib libMethClaInterface.jnilib
	cp libMethClaInterface.jnilib ../../library/macosx
	cp libMethClaInterface.jnilib /Users/wirsing/Documents/Processing/libraries/sound/library/macosx/	
	
