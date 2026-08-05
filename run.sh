#!/bin/bash
set -e

echo "Compiling Java Files..."
mkdir -p out
javac -d out \
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
java -cp out com.routeplanner.Main
