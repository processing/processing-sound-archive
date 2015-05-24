#!/bin/bash

cd ../../library/macosx/
install_name_tool -id @loader_path/libmethcla.dylib libmethcla.dylib
install_name_tool -change /usr/local/lib/libmpg123.0.dylib @loader_path/libmpg123.0.dylib libmethcla.dylib
install_name_tool -change /usr/local/lib/libsndfile.1.dylib @loader_path/libsndfile.1.dylib libmethcla.dylib 
cp libmethcla.dylib ~/Documents/Processing/libraries/sound/library/macosx
