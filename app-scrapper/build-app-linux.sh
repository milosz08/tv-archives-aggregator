#!/bin/bash

JAVA_VERSION=17
MAIN_JAR="app-scrapper-$PROJECT_VERSION.jar"
INSTALLER_TYPES=("rpm" "deb" "app-image")

rm -rfd ./target/java-runtime/
rm -rfd target/installer/

mkdir -p target/installer/input/libs/
cp target/libs/* target/installer/input/libs/
cp target/${MAIN_JAR} target/installer/input/libs/

$JAVA_HOME/bin/jlink \
  --strip-native-commands \
  --no-header-files \
  --no-man-pages  \
  --compress=2  \
  --strip-debug \
  --add-modules ALL-MODULE-PATH \
  --include-locales=en,pl \
  --output target/java-runtime

for type in "${INSTALLER_TYPES[@]}"
do
  echo "Creating installer of type $type"

  $JAVA_HOME/bin/jpackage \
  --type $type \
  --dest target/installer \
  --input target/installer/input/libs \
  --name "Archives Aggregator Scrapper" \
  --main-class pl.miloszgilga.archiver.scrapper.AppScrapperMain \
  --main-jar ${MAIN_JAR} \
  --java-options -Xmx2048m \
  --runtime-image target/java-runtime \
  --icon logo/linux/logo.png \
  --app-version ${APP_VERSION} \
  --vendor "Milosz Gilga" \
  --copyright "Copyright © 2025 by Milosz Gilga"
done
