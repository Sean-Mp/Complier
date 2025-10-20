#!/bin/bash

echo "Building SPL Compiler..."

# Clean
rm -rf out 2>/dev/null || true
mkdir -p out

echo "1. Generating lexer with JFlex..."
java -jar lib/jflex-full-1.9.1.jar -d out src/SPL.flex

echo "2. Generating parser with CUP..."
# Use -destdir instead of changing directory
java -jar lib/java-cup-11b.jar -parser Parser -symbols sym -expect 1 -destdir out src/SPL.cup

echo "3. Compiling all Java files..."
# Make sure to include the CUP JAR in the classpath during compilation
javac -cp "lib/*" -d out out/*.java src/Main.java

echo "✅ Build complete! Run with: java -cp \"out:lib/*\" Main"