#!/bin/bash
set -e

echo "== Preparing to build Standalone Executable =="
echo "1. Checking dependencies and compiling..."
mkdir -p lib out
if [ ! -f lib/flatlaf-3.2.1.jar ]; then
  curl -sL "https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2.1/flatlaf-3.2.1.jar" -o lib/flatlaf-3.2.1.jar
fi

# Find all java files and compile them
javac -cp "lib/*" -d out $(find src -name "*.java")

echo "2. Unpacking dependencies..."
cd out
jar xf ../lib/flatlaf-3.2.1.jar
rm -rf META-INF/MANIFEST.MF || true
cd ..

echo "3. Creating Manifest file..."
cat <<MANIFEST > manifest.txt
Main-Class: com.routeplanner.Main
MANIFEST

echo "4. Packaging SmartRoutePlanner.jar..."
jar cvfm SmartRoutePlanner.jar manifest.txt -C out . > /dev/null
rm manifest.txt

echo "== Deployment Successful! =="
