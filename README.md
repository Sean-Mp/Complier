# SPL Compiler - COS 341 Project

A complete compiler implementation for the Students' Programming Language (SPL) as specified in COS341 Semester Project 2025.

## 🎯 Project Status

| Phase | Status | Description |
|-------|--------|-------------|
| **Phase 1: Lexer** | ✅ **Complete** | Tokenization and lexical analysis |
| **Phase 2: Parser** | 🔄 In Progress | SLR parser with conflict resolution |
| **Phase 3: Semantic Analysis** | ⏳ Pending | Type checking and symbol tables |
| **Phase 4: Code Generation** | ⏳ Pending | Target code emission |

## 📁 Project Structure

```
SPL-Compiler/
├── Lexer/                      # Lexical analysis package
│   ├── Token.java              # Token data structure
│   ├── State.java              # Token type enumeration  
│   ├── Tokenizer.java          # Main tokenization logic
│   ├── TokenException.java     # Lexical error handling
│   └── FileReader.java         # File input processing
│
├── Parser/                     # Syntax analysis package
│   ├── ConflictResolver.java  # Shift-reduce conflict handling
│   ├── ParserTableHelper.java # Parsing table visualization
│   └── SLRParser.java          # SLR parser implementation
│
├── tests/                      # Test SPL programs
│   ├── test1.spl              # Minimal valid program
│   ├── test2.spl              # Simple sequence
│   ├── test3.spl              # Conflict test case
│   ├── test4.spl              # Control structures
│   └── test5.spl              # Full program with functions
│
├── Main.java                   # Quick lexer test
├── LexerTestSuite.java        # Comprehensive test suite
├── Makefile                    # Build automation
└── README.md                   # This file
```

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher
- JDK installed and configured
- Terminal/Command prompt access

### Installation

```bash
# Clone or download the project
cd path/to/project

# Create directory structure
mkdir -p Lexer Parser tests

# Place all source files in their respective directories
```

### Compilation

```bash
# Option 1: Using Make (Recommended)
make

# Option 2: Manual compilation
javac Lexer/*.java
javac Parser/*.java
javac Main.java
javac LexerTestSuite.java
```

### Running Tests

```bash
# Quick lexer test
make run
# or
java Main

# Comprehensive test suite
make test
# or
java LexerTestSuite

# Conflict visualization
make conflict
# or
java Parser.ParserTableHelper

# Full test cycle (clean, compile, test)
make fulltest
```

## 📚 Features

### ✅ Lexer (Complete)

The lexer tokenizes SPL source code into the following token types:

| Token Type | Description | Examples |
|------------|-------------|----------|
| **KEYWORD** | Reserved words | `glob`, `proc`, `func`, `main`, `if`, `while` |
| **VARIABLE** | User-defined names | `x`, `var1`, `myVariable` |
| **NUMBER** | Integer literals | `0`, `42`, `12345` |
| **STRING** | Text literals | `"hello"`, `"test123"` |
| **OPERATOR** | Operations | `eq`, `plus`, `minus`, `>`, `and`, `or` |
| **SYMBOL** | Delimiters | `(`, `)`, `{`, `}`, `;`, `=` |

**Lexer Features:**
- ✅ Complete token recognition for all SPL constructs
- ✅ Comment handling (`//`)
- ✅ Whitespace normalization
- ✅ Error reporting with line and column numbers
- ✅ Comprehensive validation (no leading zeros, lowercase variables, string length limits)

### 🔄 Parser (In Progress)

**Completed:**
- ✅ Shift-reduce conflict identified in ALGO productions
- ✅ Resolution strategy documented (always SHIFT on `;`)
- ✅ Conflict visualization tools
- ✅ Parser framework structure

**TODO:**
- ⏳ LR(0) automaton construction
- ⏳ ACTION and GOTO table generation
- ⏳ Parse tree building
- ⏳ Error recovery

## 🔍 The Shift-Reduce Conflict

### Problem

In the SPL grammar, the ALGO productions cause a conflict:

```
ALGO ::= INSTR
ALGO ::= INSTR ; ALGO
```

When parsing sequences like `halt ; print x ; halt`, after parsing the first `halt`, the parser sees `;` and faces a decision:

- **REDUCE**: Complete ALGO immediately (wrong - causes parse failure)
- **SHIFT**: Continue building the sequence (correct!)

### Solution

**Always SHIFT when lookahead is `;` in ALGO productions.**

This allows the parser to build complete instruction sequences before reducing to ALGO.

**Example:**
```
Input: halt ; print x ; halt

Wrong (REDUCE early):
halt → INSTR → ALGO
Then ERROR on "; print x"

Correct (SHIFT):
halt → INSTR
SHIFT ;
print x → INSTR
SHIFT ;
halt → INSTR
Then REDUCE all → ALGO ✓
```

See conflict visualization:
```bash
make conflict
```

## 🧪 Testing

### Test Suite Overview

```bash
# Run all tests
make test

# Expected output:
# ✓ 14 keyword tests
# ✓ 10 operator tests
# ✓ 6 symbol tests
# ✓ 5 number tests
# ✓ 5 variable tests
# ✓ 4 string tests
# ✓ 3 complex expression tests
# ✓ 5 error case tests
# Total: 52 tests passed
```

### Sample Test Programs

**test1.spl** - Minimal program
```spl
main {
  var { }
  halt
}
```

**test2.spl** - Tests conflict resolution
```spl
main {
  var { x }
  x = 5 ;
  print x ;
  halt
}
```

**test3.spl** - Critical conflict test
```spl
main {
  var { }
  halt ;
  print "test" ;
  halt
}
```

## 📖 Usage Examples

### Tokenizing a File

```java
import Lexer.*;

// Read and tokenize an SPL file
FileReader reader = new FileReader("program.spl");

// Get the token list
List<Token> tokens = reader.getTokenizer().getTokenList();

// Process tokens
for (Token token : tokens) {
    System.out.println(token.getState() + ": " + token.getValue());
}
```

### Using the Tokenizer Directly

```java
import Lexer.*;

Tokenizer tokenizer = new Tokenizer();

try {
    // Tokenize a single line
    tokenizer.tokenizeLine("x = 5 ;", 1);
    
    // Or process individual tokens
    tokenizer.processToken("halt");
    tokenizer.processToken(";");
    
    // Get results
    tokenizer.printTokens();
    
} catch (TokenException e) {
    System.err.println("Error: " + e.getMessage());
}
```

## 🛠️ Development

### Adding New Test Cases

```bash
# Create a new test file
cat > tests/my_test.spl << 'EOF'
main {
  var { x y }
  x = 10 ;
  y = 20 ;
  print x ;
  halt
}
EOF

# Test it
java Main tests/my_test.spl
```

### Extending the Lexer

To add a new token type:

1. Add to `State.java` enum
2. Update `Tokenizer.java` recognition logic
3. Add test cases in `LexerTestSuite.java`
4. Recompile and test

### Building the Parser

Next steps for parser implementation:

1. **Create LR(0) items** from grammar productions
2. **Build item sets** using closure and goto operations
3. **Construct ACTION table** with conflict resolution
4. **Build GOTO table** for non-terminals
5. **Implement parse()** method
6. **Test** with sample SPL programs

## 📊 Grammar Reference

### SPL Productions (Subset)

```
SPL_PROG ::= glob { VARIABLES } proc { PROCDEFS } func { FUNCDEFS } main { MAINPROG }

ALGO ::= INSTR                  ← Conflict here!
ALGO ::= INSTR ; ALGO           ← Conflict here!

INSTR ::= halt
INSTR ::= print OUTPUT
INSTR ::= ASSIGN
INSTR ::= LOOP
INSTR ::= BRANCH

ASSIGN ::= VAR = TERM
LOOP ::= while TERM { ALGO }
BRANCH ::= if TERM { ALGO } else { ALGO }

TERM ::= ATOM
TERM ::= ( UNOP TERM )
TERM ::= ( TERM BINOP TERM )
```

### Token Patterns

| Pattern | Regex | Description |
|---------|-------|-------------|
| Variable | `[a-z][a-z0-9]*` | Lowercase start, alphanumeric |
| Number | `0\|[1-9][0-9]*` | No leading zeros |
| String | `"[a-zA-Z0-9]{0,15}"` | Max 15 characters |

## 🐛 Troubleshooting

### Common Issues

**"package Lexer does not exist"**
```bash
# Ensure you're in the project root directory
pwd  # Should show: /path/to/SPL-Compiler

# Check that Lexer directory exists with .java files
ls Lexer/

# Recompile
javac Lexer/*.java
```

**"Could not find or load main class"**
```bash
# Verify .class files exist
ls *.class

# Run from correct directory with classpath
java -cp . Main
```

**"FileNotFoundException: test_program.txt"**
```bash
# Main.java creates this automatically
# Just run it once
java Main

# For custom files, provide the correct path
java Main tests/myprogram.spl
```

**Compilation errors**
```bash
# Check Java version (need 8+)
java -version

# Clean and rebuild
make clean
make all
```

### Getting Help

1. Check the inline documentation in source files
2. Run `make help` for available commands
3. Review test cases in `LexerTestSuite.java`
4. Examine conflict visualization: `make conflict`

## 📝 Makefile Commands

```bash
make           # Compile everything
make run       # Quick lexer test
make test      # Run test suite
make conflict  # Show conflict analysis
make clean     # Remove compiled files
make fulltest  # Complete test cycle
make help      # Show all commands
```

## 🎓 Assignment Compliance

### Requirements Met

- ✅ **Lexer**: Fully implements SPL token recognition
- ✅ **Token Validation**: All vocabulary rules enforced
- ✅ **Error Handling**: Line/column error reporting
- ✅ **Grammar Compliance**: Matches SPL 2025 specification
- ✅ **Conflict Identification**: ALGO shift-reduce conflict documented
- ✅ **Resolution Strategy**: Clear rule for conflict resolution

### Grammar Rules Implemented

| Rule | Implementation | Status |
|------|----------------|--------|
| Keywords | Must match exactly | ✅ |
| Variables | `[a-z][a-z0-9]*` | ✅ |
| Numbers | `0\|[1-9][0-9]*` | ✅ |
| Strings | Max 15 chars, alphanumeric | ✅ |
| Comments | `//` to end of line | ✅ |
| No blank spaces in tokens | Whitespace = token boundary | ✅ |

## 💡 Implementation Notes

### Design Decisions

1. **Token Class**: Immutable design for safety
2. **State Enum**: Type-safe token categorization
3. **Exception Handling**: Custom exception with context
4. **File Reading**: Line-by-line for better error reporting
5. **Conflict Resolution**: Precedence-based (SHIFT over REDUCE)

### Key Algorithms

**Tokenization Process:**
```
1. Read line from file
2. Remove comments (anything after //)
3. Split on whitespace and symbols
4. For each token:
   a. Check if keyword
   b. Check if operator
   c. Check if symbol
   d. Check if string literal
   e. Check if number
   f. Check if variable
   g. Else: throw TokenException
5. Add valid token to list
```

**Conflict Resolution:**
```
IF current_state == ALGO_state AND
   lookahead == ";" AND
   can_reduce_via_ALGO_INSTR
THEN
   action = SHIFT  # Always!
ELSE
   follow_normal_SLR_rules
END IF
```

## 🔗 Related Documentation

- [SPL Grammar Specification](SemesterProject2025.pdf)
- [Type Analysis Rules](Type_Analysis.pdf)
- [Conflict Visualization](run: `make conflict`)
- [Implementation Guide](Implementation_Guide.md)

## 👥 Team Information

**Course**: COS 341 - Compiler Construction  
**Project**: SPL Compiler - Phase 1 (Front-End)  
**Institution**: University of Pretoria  
**Year**: 2025

## 📅 Timeline

| Phase | Deadline | Status |
|-------|----------|--------|
| Phase 1: Lexer | Week 4 | ✅ Complete |
| Phase 2: Parser | Week 8 | 🔄 In Progress |
| Phase 3: Semantic Analysis | Week 12 | ⏳ Pending |
| Phase 4: Code Generation | Week 16 | ⏳ Pending |

## 🌟 Features Highlight

### Lexer Strengths

- **Robust Error Handling**: Precise error locations
- **Complete Validation**: All SPL rules enforced
- **Efficient Processing**: Single-pass tokenization
- **Clean Design**: Well-structured OOP implementation
- **Comprehensive Testing**: 52 test cases covering all scenarios

### Parser Readiness

- **Conflict Identified**: Clear understanding of ambiguity
- **Solution Documented**: Resolution rule defined
- **Framework Ready**: Parser skeleton implemented
- **Visualization Tools**: Debug and understand conflicts

## 🚧 Future Enhancements

### Short-term (Phase 2)
- [ ] Complete LR(0) automaton construction
- [ ] Generate parsing tables automatically
- [ ] Implement full parse tree building
- [ ] Add parse tree visualization

### Long-term (Phases 3-4)
- [ ] Symbol table management
- [ ] Type checking implementation
- [ ] Intermediate code generation
- [ ] Target code optimization
- [ ] Full compiler pipeline

## 📞 Support

### Resources

- **Project Documentation**: See `/docs` folder
- **Test Cases**: See `/tests` folder
- **Example Programs**: Run `make testfiles`
- **Conflict Analysis**: Run `make conflict`

### Common Commands Cheat Sheet

```bash
# Setup
make setup          # Create directory structure
make testfiles      # Create sample SPL programs

# Development
make                # Build everything
make clean          # Clean build
javac Lexer/*.java  # Compile just lexer

# Testing
make run            # Quick test
make test           # Full test suite
make fulltest       # Clean + compile + test

# Analysis
make conflict       # Show shift-reduce conflict
java Parser.ParserTableHelper  # Detailed conflict info

# Custom testing
java Main tests/myprogram.spl  # Test specific file
```

## 🎯 Success Criteria Checklist

### Lexer Phase ✅
- [x] Recognizes all 14 keywords
- [x] Recognizes all 10 operators  
- [x] Recognizes all 6 symbols
- [x] Validates numbers (no leading zeros)
- [x] Validates variables (lowercase, alphanumeric)
- [x] Validates strings (max 15 chars)
- [x] Handles comments correctly
- [x] Reports errors with line/column
- [x] Passes all test cases

### Parser Phase 🔄
- [x] Conflict identified
- [x] Conflict understood
- [x] Resolution rule defined
- [ ] Parsing tables constructed
- [ ] Parse method implemented
- [ ] Parse tree generated
- [ ] All test cases pass

## 📖 Code Examples

### Example 1: Basic Tokenization

```java
import Lexer.*;

public class Example1 {
    public static void main(String[] args) {
        try {
            Tokenizer t = new Tokenizer();
            t.tokenizeLine("x = 5 ; halt", 1);
            
            for (Token token : t.getTokenList()) {
                System.out.printf("%s: %s%n", 
                    token.getState(), 
                    token.getValue());
            }
        } catch (TokenException e) {
            System.err.println(e.getMessage());
        }
    }
}

// Output:
// VARIABLE: x
// SYMBOL: =
// NUMBER: 5
// SYMBOL: ;
// KEYWORD: halt
```

### Example 2: File Processing

```java
import Lexer.*;

public class Example2 {
    public static void main(String[] args) {
        FileReader reader = new FileReader("program.spl");
        
        if (reader.getTokenizer() != null) {
            System.out.println("Tokens found: " + 
                reader.getTokenizer().getTokenList().size());
            
            reader.getTokenizer().printTokens();
        }
    }
}
```

### Example 3: Error Handling

```java
import Lexer.*;

public class Example3 {
    public static void main(String[] args) {
        Tokenizer t = new Tokenizer();
        
        try {
            // This will fail - uppercase not allowed
            t.processToken("Variable");
        } catch (TokenException e) {
            System.out.println("Caught error:");
            System.out.println("  Token: " + e.getToken());
            System.out.println("  Line: " + e.getLine());
            System.out.println("  Column: " + e.getColumn());
        }
    }
}
```

## 🏆 Project Highlights

### Achievements

✅ **Complete Lexer**: Production-ready tokenization  
✅ **Robust Testing**: Comprehensive test coverage  
✅ **Clear Documentation**: Well-documented codebase  
✅ **Conflict Resolution**: Parser conflict identified and solved  
✅ **Professional Structure**: Clean project organization  

### Quality Metrics

- **Test Coverage**: 52 test cases (100% of lexer functionality)
- **Code Quality**: Well-commented, following Java conventions
- **Error Handling**: Comprehensive with precise locations
- **Documentation**: Complete README, inline docs, guides

## 📚 Learning Outcomes

From this project, you will understand:

1. **Lexical Analysis**: Token recognition and classification
2. **Regular Expressions**: Pattern matching for language constructs
3. **Error Handling**: Providing useful feedback to users
4. **Parser Conflicts**: Understanding ambiguity in grammars
5. **Conflict Resolution**: Strategies for resolving parser conflicts
6. **LR Parsing**: Bottom-up parsing techniques
7. **Grammar Design**: Trade-offs in language specification

## 🎓 Academic Integrity

This project is for educational purposes as part of COS 341. Remember:

- Understand every line of code you submit
- Properly attribute any external resources
- Work collaboratively within your assigned group
- Test thoroughly before submission

## 📄 License

This project is created for academic purposes as part of COS 341 coursework at the University of Pretoria.

---

## 🚀 Getting Started Checklist

Ready to begin? Follow this checklist:

- [ ] Java 8+ installed (`java -version`)
- [ ] Project directory created
- [ ] All source files in correct folders
- [ ] Successfully compiled (`make` or `javac`)
- [ ] Lexer tests passing (`make test`)
- [ ] Understand the shift-reduce conflict (`make conflict`)
- [ ] Ready to implement parser

---

**Last Updated**: October 2025  
**Version**: 1.0 (Phase 1 Complete)  
**Status**: Lexer ✅ | Parser 🔄 | Semantic Analysis ⏳ | Code Gen ⏳

For questions or issues, refer to the inline documentation or run `make help`.