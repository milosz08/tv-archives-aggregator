@ECHO OFF

set JAVA_VERSION=17
set MAIN_JAR=app-scrapper-%PROJECT_VERSION%.jar
set INSTALLER_TYPE=msi

IF EXIST target\java-runtime rmdir /S /Q .\target\java-runtime
IF EXIST target\installer rmdir /S /Q target\installer

xcopy /S /Q target\libs\* target\installer\input\libs\
copy target\%MAIN_JAR% target\installer\input\libs\

call "%JAVA_HOME%\bin\jlink" ^
  --strip-native-commands ^
  --no-header-files ^
  --no-man-pages ^
  --compress=2 ^
  --strip-debug ^
  --add-modules ALL-MODULE-PATH ^
  --include-locales=en,pl ^
  --output target/java-runtime

call "%JAVA_HOME%\bin\jpackage" ^
  --type %INSTALLER_TYPE% ^
  --dest target/installer ^
  --input target/installer/input/libs ^
  --name "Archives Aggregator Scrapper" ^
  --main-class pl.miloszgilga.archiver.scrapper.AppScrapperMain ^
  --main-jar %MAIN_JAR% ^
  --java-options -Xmx2048m ^
  --runtime-image target/java-runtime ^
  --icon logo/windows/logo.ico ^
  --app-version %APP_VERSION% ^
  --vendor "Milosz Gilga" ^
  --copyright "Copyright © 2025 by Milosz Gilga" ^
  --win-dir-chooser ^
  --win-shortcut ^
  --win-per-user-install ^
  --win-menu
