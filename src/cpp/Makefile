all: 	
	g++ -I/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers -I./include -std=c++11 -g -c processing_sound_MethClaInterface.cpp;		
	g++ -dynamiclib -lmethcla -L../../library/macosx/ -o libMethClaInterface.jnilib *.o;

clean:
	rm *.o
	rm *.jnilib

install:
	install_name_tool -change @executable_path/libmethcla.dylib  @loader_path/libmethcla.dylib libMethClaInterface.jnilib
	cp libMethClaInterface.jnilib ../../library/macosx
	cp libMethClaInterface.jnilib /Users/wirsing/Documents/Processing/libraries/sound/library/macosx/	
	
