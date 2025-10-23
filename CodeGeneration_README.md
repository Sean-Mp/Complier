# SPL Compiler Project

## Overview

 The final output is **intermediate BASIC-style code** that can be executed on online BASIC interpreters such as [jsbasic](https://www.calormen.com/jsbasic/).

---

## Build and Run Instructions

### 1. Compile All Components

Run the following commands from the project root:

```bash
# 1. Compile the AST
javac -cp "lib\*;out" -d out src/AllAST.java

# 2. Generate the lexer (JFlex)
java -jar lib/jflex-full-1.9.1.jar -d out src/SPL.flex

# 3. Generate the parser (CUP)
java -jar lib/java-cup-11b.jar -parser Parser -symbols sym -expect 1 -destdir out src/SPL.cup

# 4. Compile the symbol table
javac -cp "lib\*;out" -d out src/symboltable/*.java

# 5. Compile the type analyzer
javac -cp "lib\*;out" -d out src/TypeAnalyzer.java 

# 6. Compile the code generator
javac -cp "lib\*;out" -d out src/CodeGenerator.java

# 7. Compile the main driver
javac -cp "lib\*;out" -d out src/Main.java
```

---

### 2. Run the Compiler

To compile an SPL source file (e.g. `comprehensive_test.spl`):

```bash
java -cp "out;lib\*" Main comprehensive_test.spl
```

This performs **lexical**, **syntax**, **semantic**, and **type analysis**, then generates intermediate code saved as:

```
comprehensive_test_ic.txt
```


## 🧠 Code Generation Approach

The **CodeGenerator** class implements a visitor-based approach that traverses the AST and emits intermediate BASIC-like instructions.

* **Expressions** are evaluated recursively, with temporary variables (`t0`, `t1`, …) holding intermediate results.
* **Control flow** (`if`, `while`, `do-until`) is implemented using auto-generated labels (`L0`, `L1`, …).
* **Boolean logic** and **comparison operators** are lowered to `IF ... THEN GOTO` statements.
* **Procedures** and **functions** are stored for later invocation using `CALL` instructions.
* **Output** includes comments (`REM`) for readability and structure.

This design makes the output both **interpretable** and **machine-readable**, bridging SPL’s high-level structure and a BASIC-style execution model.

---

