# SPL Compiler Makefile
# Usage: make [target]

# Compiler
JAVAC = javac
JAVA = java

# Directories
LEXER_DIR = Lexer
PARSER_DIR = Parser
TESTS_DIR = tests

# Source files
LEXER_SRC = $(wildcard $(LEXER_DIR)/*.java)
PARSER_SRC = $(wildcard $(PARSER_DIR)/*.java)
MAIN_SRC = Main.java
TEST_SRC = LexerTestSuite.java

# Class files
LEXER_CLASS = $(LEXER_SRC:.java=.class)
PARSER_CLASS = $(PARSER_SRC:.java=.class)
MAIN_CLASS = Main.class
TEST_CLASS = LexerTestSuite.class

# Colors for output
GREEN = \033[0;32m
YELLOW = \033[1;33m
RED = \033[0;31m
NC = \033[0m # No Color

# Default target
.DEFAULT_GOAL := all

# Phony targets
.PHONY: all clean test run suite conflict help setup dirs

# Help target
help:
	@echo "$(YELLOW)SPL Compiler - Makefile Targets$(NC)"
	@echo "=================================="
	@echo "$(GREEN)make$(NC)          - Compile everything (default)"
	@echo "$(GREEN)make all$(NC)      - Compile everything"
	@echo "$(GREEN)make compile$(NC)  - Compile all source files"
	@echo "$(GREEN)make run$(NC)      - Run quick lexer test"
	@echo "$(GREEN)make test$(NC)     - Run comprehensive test suite"
	@echo "$(GREEN)make suite$(NC)    - Run LexerTestSuite"
	@echo "$(GREEN)make conflict$(NC) - Show conflict visualization"
	@echo "$(GREEN)make clean$(NC)    - Remove all .class files"
	@echo "$(GREEN)make setup$(NC)    - Create directory structure"
	@echo "$(GREEN)make testfiles$(NC)- Create sample SPL test files"
	@echo "$(GREEN)make fulltest$(NC) - Clean, compile, and test everything"
	@echo ""

# Compile everything
all: compile
	@echo "$(GREEN)✓ Build complete!$(NC)"

# Compile all source files
compile: $(LEXER_CLASS) $(PARSER_CLASS) $(MAIN_CLASS) $(TEST_CLASS)
	@echo "$(GREEN)✓ All files compiled successfully$(NC)"

# Compile Lexer
$(LEXER_DIR)/%.class: $(LEXER_DIR)/%.java
	@echo "$(YELLOW)Compiling Lexer...$(NC)"
	$(JAVAC) $(LEXER_DIR)/*.java

# Compile Parser
$(PARSER_DIR)/%.class: $(PARSER_DIR)/%.java
	@echo "$(YELLOW)Compiling Parser...$(NC)"
	$(JAVAC) $(PARSER_DIR)/*.java

# Compile Main
$(MAIN_CLASS): $(MAIN_SRC) $(LEXER_CLASS)
	@echo "$(YELLOW)Compiling Main...$(NC)"
	$(JAVAC) $(MAIN_SRC)

# Compile Test Suite
$(TEST_CLASS): $(TEST_SRC) $(LEXER_CLASS)
	@echo "$(YELLOW)Compiling Test Suite...$(NC)"
	$(JAVAC) $(TEST_SRC)

# Run quick test
run: $(MAIN_CLASS)
	@echo "$(YELLOW)Running quick lexer test...$(NC)"
	@echo ""
	$(JAVA) Main
	@echo ""

# Run test suite
test: suite

suite: $(TEST_CLASS)
	@echo "$(YELLOW)Running comprehensive test suite...$(NC)"
	@echo ""
	$(JAVA) LexerTestSuite
	@echo ""

# Show conflict visualization
conflict: $(PARSER_DIR)/ParserTableHelper.class
	@echo "$(YELLOW)Displaying conflict analysis...$(NC)"
	@echo ""
	$(JAVA) $(PARSER_DIR).ParserTableHelper
	@echo ""

# Full test: clean, compile, and test
fulltest: clean all
	@echo "$(GREEN)================================$(NC)"
	@echo "$(GREEN)Running Full Test Suite$(NC)"
	@echo "$(GREEN)================================$(NC)"
	@echo ""
	@$(MAKE) run
	@$(MAKE) suite
	@$(MAKE) conflict

# Clean compiled files
clean:
	@echo "$(YELLOW)Cleaning compiled files...$(NC)"
	del /Q $(LEXER_DIR)\*.class 2>nul
	del /Q $(PARSER_DIR)\*.class 2>nul
	del /Q *.class 2>nul
	del /Q test_program.txt 2>nul
	@echo "$(GREEN)✓ Clean complete$(NC)"

# Setup directory structure
setup: dirs
	@echo "$(GREEN)✓ Directory structure created$(NC)"

dirs:
	@echo "$(YELLOW)Creating directory structure...$(NC)"
	mkdir -p $(LEXER_DIR)
	mkdir -p $(PARSER_DIR)
	mkdir -p $(TESTS_DIR)

# Create sample test files
testfiles:
	@echo "$(YELLOW)Creating sample SPL test files...$(NC)"
	@mkdir -p $(TESTS_DIR)
	@echo "main {\n  var { }\n  halt\n}" > $(TESTS_DIR)/test1.spl
	@echo "main {\n  var { x }\n  x = 5 ;\n  print x ;\n  halt\n}" > $(TESTS_DIR)/test2.spl
	@echo "main {\n  var { }\n  halt ;\n  print \"test\" ;\n  halt\n}" > $(TESTS_DIR)/test3.spl
	@echo "$(GREEN)✓ Test files created in $(TESTS_DIR)/$(NC)"

# Quick reference
quickref:
	@echo "$(YELLOW)Quick Reference:$(NC)"
	@echo "  make          → Compile everything"
	@echo "  make run      → Test lexer"
	@echo "  make test     → Run all tests"
	@echo "  make clean    → Remove .class files"
	@echo "  make fulltest → Complete test cycle"