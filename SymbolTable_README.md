1. Symbol Class
Purpose: Represents a single entry in the symbol table.

Key Responsibilities:

Stores metadata about identifiers (variables, functions, etc.)
Tracks the symbol's name, type, and scope level
Manages memory allocation information (offset)
Handles special attributes (like function parameters)

Key Methods:

getName(): Returns the symbol's name
getType(): Returns the symbol's data type
getOffset(): Gets the memory offset
setOffset(): Sets the memory offset
isParameter(): Checks if the symbol is a function parameter

2. Scope Class
Purpose: Represents a lexical scope in the program.

Key Responsibilities:

Maintains a collection of symbols within a single scope
Manages parent-child relationships between scopes
Handles variable shadowing
Tracks memory allocation within the scope

Key Methods:

addSymbol(Symbol): Adds a symbol to this scope
lookup(String): Looks up a symbol in the current scope only
lookupAll(String): Recursively looks up a symbol in this and all parent scopes
getParentScope(): Returns the parent scope
getLevel(): Returns the nesting level

3. SymbolTable Class
Purpose: Manages the hierarchy of scopes and symbol lookups.

Key Responsibilities:

Manages the current scope
Handles scope entry/exit
Provides interface for symbol insertion and lookup
Maintains scope nesting level

Key Methods:

enterScope(): Creates and enters a new nested scope
exitScope(): Leaves the current scope
insert(Symbol): Adds a symbol to the current scope
lookup(String): Looks up a symbol in the current scope only
lookupAll(String): Recursively searches for a symbol
getCurrentScope(): Returns the current scope
getCurrentLevel(): Returns the current nesting level

4. SemanticError Class
Purpose: Represents semantic errors found during analysis.

Key Responsibilities:

Signals semantic errors (like undefined variables, type mismatches)
Provides meaningful error messages
Extends RuntimeException for easy error propagation

5. SemanticAnalyzer Class
Purpose: Performs semantic analysis of the program.

Key Responsibilities:

Validates type compatibility
Ensures variables are declared before use
Checks function calls and returns
Verifies scope rules
Detects duplicate declarations

Key Methods:

visitVariableDeclaration(): Processes variable declarations
visitFunctionDeclaration(): Processes function declarations
visitAssignment(): Validates assignments
checkTypeCompatibility(): Verifies type compatibility
reportError(): Reports semantic errors

6. SymbolTableTest Class
Purpose: Unit tests for the symbol table implementation.

Key Responsibilities:

Verifies correct behavior of symbol table operations
Tests scope nesting and symbol lookup
Validates error detection
Ensures proper memory offset calculation

Purpose: Performs semantic analysis of the program.

Key Responsibilities:

Validates type compatibility
Ensures variables are declared before use
Checks function calls and returns
Verifies scope rules
Detects duplicate declarations

Key Methods:

visitVariableDeclaration(): Processes variable declarations
visitFunctionDeclaration(): Processes function declarations
visitAssignment(): Validates assignments
checkTypeCompatibility(): Verifies type compatibility
reportError(): Reports semantic errors

Purpose: Unit tests for the symbol table implementation.

Key Responsibilities:

Verifies correct behavior of symbol table operations
Tests scope nesting and symbol lookup
Validates error detection
Ensures proper memory offset calculation
How They Work Together:
During Parsing:
The parser creates symbols for each declaration
Calls 
symbolTable.enterScope()
 when entering a new scope
Inserts symbols using symbolTable.insert()
Calls 
symbolTable.exitScope()
 when leaving a scope
During Semantic Analysis:
The SemanticAnalyzer uses the SymbolTable to:
Check variable declarations
Verify types
Validate scoping rules