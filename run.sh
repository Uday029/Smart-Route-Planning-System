#!/bin/bash
set -e

echo "Checking for UI dependencies..."
mkdir -p lib
if [ ! -f lib/flatlaf-3.2.1.jar ]; then
  curl -sL "https://repo1.maven.org/maven2/com/formdev/flatlaf/flatlaf/3.2.1/flatlaf-3.2.1.jar" -o lib/flatlaf-3.2.1.jar
fi

echo "Compiling Java Files..."
mkdir -p out
javac -cp "lib/*" -d out \
  src/main/java/com/routeplanner/model/*.java \
  src/main/java/com/routeplanner/dsa/*.java \
  src/main/java/com/routeplanner/dao/*.java \
  src/main/java/com/routeplanner/manager/*.java \
  src/main/java/com/routeplanner/util/*.java \
  src/main/java/com/routeplanner/ui/*.java \
  src/main/java/com/routeplanner/Main.java

echo "Copying resources..."
cp src/main/resources/db.properties out/ || true

echo "Starting Application..."
java -cp "out:lib/*" com.routeplanner.Main
