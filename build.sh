#!/bin/bash

echo "Building SPL Compiler..."

# Clean only generated files
rm -f out/Lexer.java out/Parser.java out/sym.java 2>/dev/null || true
rm -rf out 2>/dev/null || true
mkdir -p out

echo "1. Extracting CUP runtime classes..."
cd lib && jar xf java-cup-11b.jar && cd ..
mv lib/java_cup lib/runtime out/ 2>/dev/null || true

echo "2. Compiling AllAST.java first..."
javac -cp "lib/*" -d out src/AllAST.java

echo "3. Generating lexer with JFlex..."
java -jar lib/jflex-full-1.9.1.jar -d out src/SPL.flex

echo "4. Generating parser with CUP..."
java -jar lib/java-cup-11b.jar -parser Parser -symbols sym -expect 1 -destdir out src/SPL.cup

echo "5. Compiling symbol table classes..."
javac -cp "lib/*:out" -d out src/symboltable/*.java

echo "6. Compiling TypeAnalyzer..."
javac -cp "lib/*:out" -d out src/TypeAnalyzer.java

echo "7. Compiling CodeGenerator..."
javac -cp "lib/*:out" -d out src/CodeGenerator.java

echo "8. Compiling BasicPostProcessor..."
javac -cp "lib/*:out" -d out src/BasicPostProcessor.java

echo "9. Compiling everything else..."
javac -cp "lib/*:out" -d out out/*.java src/Main.java

echo "10. Creating executable JAR..."
# Use either the -m flag with manifest OR -e flag for main class, not both
jar cfm SPLCompiler.jar manifest.txt -C out .

echo "✓ Build complete!"
echo "Run with: java -jar SPLCompiler.jar <input_file.txt>"