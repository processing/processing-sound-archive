module Main where

import           Control.Arrow (second)
import           Control.Monad (when)
import           Data.Char (toLower)
import           Development.Shake
import           Development.Shake.FilePath
import           Development.Shake.Language.C
import qualified Development.Shake.Language.C.BuildFlags as BuildFlags
import qualified Development.Shake.Language.C.Host as Host
import           Development.Shake.Language.C.Label
import qualified Development.Shake.Language.C.PkgConfig as PkgConfig
import qualified Development.Shake.Language.C.Target.OSX as OSX
import qualified Development.Shake.Language.C.Config as Config
import System.Console.GetOpt

data Options = Options {
    javaIncludeDirectory :: String
  , targetArchitecture :: String
  } deriving (Eq, Show)

flags = [ Option "" ["java-includes"]
                 (ReqArg (\a -> Right (\o -> o { javaIncludeDirectory = a })) "DIRECTORY")
                 "Java include directory."
        , Option "" ["arch"]
                 (ReqArg (\a -> case a of
                                  "32" -> Right (\o -> o { targetArchitecture = a })
                                  "64" -> Right (\o -> o { targetArchitecture = a })
                                  _ -> Left $ "Invalid architecture " ++ show a) "32|64")
                 "Target architecture."
        ]

buildDir :: FilePath
buildDir = "build"

targetDirectory :: OS -> String -> String
targetDirectory Linux arch = "linux" ++ arch
targetDirectory OSX _ = "macosx"
targetDirectory Windows arch = "windows" ++ arch
targetDirectory os _ = error $ "Unsupported target OS " ++ show os

sharedLibraryExtension :: OS -> String
sharedLibraryExtension Linux = "so"
sharedLibraryExtension OSX = "jnilib"
sharedLibraryExtension Windows = "dll"
sharedLibraryExtension os = error $ "Unsupported target OS " ++ show os

main :: IO ()
main = shakeArgsWith shakeOptions { shakeFiles = buildDir } flags $ \flags targets -> return $ Just $ do
  let options = foldl (.) id flags $ Options "" "64"

  -- Write build options to config file included in build system configuration
  "build/build.cfg" %> \out -> do
    alwaysRerun
    writeFileChanged out $ unlines [
        "targetArchitecture = " ++ targetArchitecture options
      , "javaIncludeDirectory = " ++ javaIncludeDirectory options
      ]

  -- Get build configuration with generated config file dependency
  withConfig <- Config.withConfig ["build/build.cfg"]

  let (target, toolChain) = second ((=<<) applyEnv) Host.defaultToolChain
      getConfig = withConfig [
          ("Target.os", map toLower . show . targetOS $ target)
        ] "config/library.cfg"
      buildPrefix = buildDir </> toBuildPrefix target </> "arch" ++ targetArchitecture options
      build f ext =
        f toolChain (buildPrefix </> "libMethClaInterface" <.> ext)
                    (BuildFlags.fromConfig getConfig)
                    (Config.getPaths getConfig ["Sources"])
  sharedLib <- build sharedLibrary (sharedLibraryExtension (targetOS target))

  let installedLib = "library" </> targetDirectory (targetOS target) (targetArchitecture options) </> takeFileName sharedLib
  installedLib %> \out -> do
    copyFile' sharedLib out
    when (targetOS target == OSX) $
      cmd "install_name_tool -change @loader_path/libmethcla.dylib @executable_path/libmethcla.dylib" out

  phony "lib" $ need [sharedLib]
  phony "install" $ need [installedLib]
  phony "clean" $ removeFilesAfter buildDir ["//*"]

  want targets
