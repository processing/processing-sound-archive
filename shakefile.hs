module Main where

import           Control.Arrow (second)
import           Control.Monad (forM_, when)
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

data TargetArchitecture = Arch32 | Arch64 deriving (Eq, Show)

data Options = Options {
    javaIncludeDirectory :: String
  , targetArchitecture :: TargetArchitecture
  } deriving (Eq, Show)

flags = [ Option "" ["java-includes"]
                 (ReqArg (\a -> Right (\o -> o { javaIncludeDirectory = a })) "DIRECTORY")
                 "Java include directory."
        , Option "" ["arch"]
                 (ReqArg (\a -> case a of
                                  "32" -> Right (\o -> o { targetArchitecture = Arch32 })
                                  "64" -> Right (\o -> o { targetArchitecture = Arch64 })
                                  _ -> Left $ "Invalid architecture " ++ show a) "32|64")
                 "Target architecture."
        ]

buildDir :: FilePath
buildDir = "build"

targetDirectory :: OS -> TargetArchitecture -> String
targetDirectory Linux Arch32 = "linux32"
targetDirectory Linux Arch64 = "linux64"
targetDirectory OSX _ = "macosx"
targetDirectory Windows Arch32 = "windows32"
targetDirectory Windows Arch64 = "windows64"
targetDirectory os _ = error $ "Unsupported target OS " ++ show os

sharedLibraryExtension :: OS -> String
sharedLibraryExtension Linux = "so"
sharedLibraryExtension OSX = "jnilib"
sharedLibraryExtension Windows = "dll"
sharedLibraryExtension os = error $ "Unsupported target OS " ++ show os

methcla :: String -> Action ()
methcla = cmd [Cwd "methcla", Shell] "./shake -c release"

methclaTargets :: OS -> TargetArchitecture -> [String]
methclaTargets os arch =
  case os of
    Linux -> case arch of
               Arch32 -> products ["linux/i686/libmethcla.so"]
               Arch64 -> products ["linux/x86_64/libmethcla.so"]
    OSX -> products ["macosx/x86_64/libmethcla.dylib"]
    Windows -> case arch of
                 Arch32 -> products ["windows/i686/libmethcla.dll"]
                 Arch64 -> products ["windows/x86_64/libmethcla.dll"]
    _ -> error $ "Unsupported target OS " ++ show os
  where products = map ("methcla/build/release" </>)

main :: IO ()
main = shakeArgsWith shakeOptions { shakeFiles = buildDir } flags $ \flags targets -> return $ Just $ do
  let options = foldl (.) id flags $ Options "" Arch64

  -- Build Methcla targets
  "methcla/build//*" %> \target -> do
    -- Treat methcla targets as phony
    alwaysRerun
    methcla (dropDirectory1 target)

  -- Write build options to config file included in build system configuration
  "build/build.cfg" %> \out -> do
    alwaysRerun
    writeFileChanged out $ unlines [
        "targetArchitecture = " ++ case targetArchitecture options of
                                     Arch32 -> "32"
                                     Arch64 -> "64"
      , "javaIncludeDirectory = " ++ javaIncludeDirectory options
      ]

  -- Get build configuration with generated config file dependency
  withConfig <- Config.withConfig ["build/build.cfg"]

  let (target, toolChain) = second ((=<<) applyEnv) Host.defaultToolChain
      getConfig = withConfig [
          ("Target.os", map toLower . show . targetOS $ target)
        ] "config/library.cfg"
      buildPrefix = buildDir </> toBuildPrefix target </> map toLower (show (targetArchitecture options))
      build f ext =
        f toolChain (buildPrefix </> "libMethClaInterface" <.> ext)
                    (BuildFlags.fromConfig getConfig)
                    (Config.getPaths getConfig ["Sources"])
  sharedLib <- build sharedLibrary (sharedLibraryExtension (targetOS target))

  let installed x = "library" </> targetDirectory (targetOS target) (targetArchitecture options) </> takeFileName x
      methclaLibs = methclaTargets (targetOS target) (targetArchitecture options)

  installed sharedLib %> copyFile' sharedLib
  forM_ methclaLibs $ \lib ->
    installed lib %> copyFile' lib
    -- when (targetOS target == OSX) $
    --   cmd "install_name_tool -change @loader_path/libmethcla.dylib @executable_path/libmethcla.dylib" out

  phony "lib" $ need [sharedLib]
  phony "install" $ need $ [installed sharedLib] ++ map installed methclaLibs
  phony "clean" $ methcla "clean" >> removeFilesAfter buildDir ["//*"]

  want targets
