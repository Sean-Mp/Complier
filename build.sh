#!/bin/bash

echo "Building SPL Compiler..."

# Clean only generated files
rm -f out/Lexer.java out/Parser.java out/sym.java 2>/dev/null || true
rm -rf out 2>/dev/null || true
mkdir -p out

echo "1. Generating lexer with JFlex..."
java -jar lib/jflex-full-1.9.1.jar -d out src/SPL.flex

echo "2. Generating parser with CUP..."
cd out
java -jar ../lib/java-cup-11b.jar -parser Parser -symbols sym -expect 1 ../src/SPL.cup
cd ..

echo "3. Compiling all Java files..."
javac -cp "lib/*:out" -d out out/Lexer.java out/Parser.java src/Main.java

echo "✅ Build complete! Run with: java -cp \"out:lib/*\" Main"