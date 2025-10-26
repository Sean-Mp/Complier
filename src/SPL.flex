import java_cup.runtime.*;

%%

%class Lexer
%cup
%unicode
%line
%column
%public

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace = [ \t\r\n\u000B\u000C\u0085\u00A0\u2028\u2029]
Identifier = [a-z][a-z0-9]*
Number = 0 | [1-9][0-9]*
String = \"[^\"]{1,15}\"
Comment = "//" [^\r\n]*

%%

<YYINITIAL> {
    "glob"     { return symbol(sym.GLOB); }
    "proc"     { return symbol(sym.PROC); }
    "func"     { return symbol(sym.FUNC); }
    "main"     { return symbol(sym.MAIN); }
    "local"    { return symbol(sym.LOCAL); }
    "var"      { return symbol(sym.VAR); }
    "return"   { return symbol(sym.RETURN_KW); }
    "halt"     { return symbol(sym.HALT); }
    "print"    { return symbol(sym.PRINT); }
    "while"    { return symbol(sym.WHILE_KW); }
    "do"       { return symbol(sym.DO_KW); }
    "until"    { return symbol(sym.UNTIL); }
    "if"       { return symbol(sym.IF_KW); }
    "else"     { return symbol(sym.ELSE_KW); }
    "neg"      { return symbol(sym.NEG); }
    "not"      { return symbol(sym.NOT); }
    "eq"       { return symbol(sym.EQ); }
    "or"       { return symbol(sym.OR); }
    "and"      { return symbol(sym.AND); }
    "plus"     { return symbol(sym.PLUS); }
    "minus"    { return symbol(sym.MINUS); }
    "mult"     { return symbol(sym.MULT); }
    "div"      { return symbol(sym.DIV); }
    ">"        { return symbol(sym.GT); }
    "="        { return symbol(sym.ASSIGN_OP); }
    ";"        { return symbol(sym.SEMI); }
    "("        { return symbol(sym.LPAREN); }
    ")"        { return symbol(sym.RPAREN); }
    "{"        { return symbol(sym.LBRACE); }
    "}"        { return symbol(sym.RBRACE); }
    
    {Identifier} { return symbol(sym.USER_DEFINED_NAME, yytext()); }
    {Number}     { return symbol(sym.NUMBER, Integer.parseInt(yytext())); }
    {String}     { 
        String text = yytext();
        return symbol(sym.STRING, text.substring(1, text.length()-1)); 
    }
    
    {WhiteSpace} { /* ignore */ }
    {Comment}    { /* ignore */ }
}

[^] { throw new Error("Illegal character: " + yytext()); }