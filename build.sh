#!/bin/bash

echo "Building SPL Compiler..."

# Clean only generated files
rm -f out/Lexer.java out/Parser.java out/sym.java 2>/dev/null || true
rm -rf out 2>/dev/null || true
mkdir -p out

echo "1. Compiling AllAST.java first..."
javac -cp "lib/*" -d out src/AllAST.java

echo "2. Generating lexer with JFlex..."
java -jar lib/jflex-full-1.9.1.jar -d out src/SPL.flex

echo "3. Generating parser with CUP..."
java -jar lib/java-cup-11b.jar -parser Parser -symbols sym -expect 1 -destdir out src/SPL.cup

echo "4. Compiling symbol table classes..."
javac -cp "lib/*:out" -d out src/symboltable/*.java

echo "5. Compiling TypeAnalyzer..."
javac -cp "lib/*:out" -d out src/TypeAnalyzer.java


echo "6. Compiling CodeGenerator..."
javac -cp "lib/*:out" -d out src/CodeGenerator.java

echo "7. Compiling everything else..."
javac -cp "lib/*:out" -d out out/*.java src/Main.java

echo " Build complete! Run with: java -cp \"out:lib/*\" Main"