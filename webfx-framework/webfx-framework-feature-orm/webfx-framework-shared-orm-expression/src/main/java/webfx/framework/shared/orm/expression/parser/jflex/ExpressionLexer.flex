
/* --------------------------Usercode Section------------------------ */

package webfx.framework.shared.orm.expression.parser.jflex;

import java_cup.runtime.*;
import webfx.framework.shared.orm.expression.parser.javacup.ExpressionSymbols;
import static webfx.framework.shared.orm.expression.parser.javacup.ExpressionSymbols.*;

%%
   
/* -----------------Options and Declarations Section----------------- */

%public
%class ExpressionLexer
%line
%column
%cupsym ExpressionSymbols
%cup

/*
  Declarations
*/
%{   
    StringBuilder string = new StringBuilder();

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}
   

/*
  Macro Declarations
*/
   
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]
IntegerLiteral = 0 | [1-9][0-9]*
Identifier = [A-Za-z_][A-Za-z_0-9]*
StringSingleQuoteCharacter = [^\r\n'\\]
StringDoubleQuoteCharacter = [^\r\n\"\\]
StringGraveAccentCharacter = [^\r\n`\\]
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
OctDigit = [0-7]
GroupBy = [Gg][Rr][Oo][Uu][Pp]{WhiteSpace}+[Bb][Yy]
OrderBy = [Oo][Rr][Dd][Ee][Rr]{WhiteSpace}+[Bb][Yy]

%state STRING_SINGLE_QUOTE, STRING_DOUBLE_QUOTE, STRING_GRAVE_ACCENT

%%
/* ------------------------Lexical Rules Section---------------------- */
   

<YYINITIAL> {

  /* keywords */
    "select"                       { return symbol(SELECT); }
    "distinct"                     { return symbol(DISTINCT); }
    "from"                         { return symbol(FROM); }
    "where"                        { return symbol(WHERE); }
    {GroupBy}                      { return symbol(GROUP_BY); }
    "having"                       { return symbol(HAVING); }
    {OrderBy}                      { return symbol(ORDER_BY); }
    "desc"                         { return symbol(DESC); }
    "asc"                          { return symbol(ASC); }
    "nulls"                        { return symbol(NULLS); }
    "first"                        { return symbol(FIRST); }
    "last"                         { return symbol(LAST); }
    "limit"                        { return symbol(LIMIT); }
    "insert"                       { return symbol(INSERT); }
    "update"                       { return symbol(UPDATE); }
    "delete"                       { return symbol(DELETE); }
    "set"                          { return symbol(SET); }
    "exists"                       { return symbol(EXISTS); }
    "is"                           { return symbol(IS); }
    "like"                         { return symbol(LIKE); }
    "in"                           { return symbol(IN); }
    "as"                           { return symbol(AS); }
    "any"                          { return symbol(ANY); }
    "all"                          { return symbol(ALL); }
    "this"                         { return symbol(THIS); }

  /* internal usage parser tokens */

    "expr:="                       { return symbol(EXPR_START); }


    {IntegerLiteral}               { return symbol(NUMBER, Integer.valueOf(yytext())); }

  /* boolean literals */
    "true"                         { return symbol(BOOLEAN, Boolean.TRUE); }
    "false"                        { return symbol(BOOLEAN, Boolean.FALSE); }

  /* null literal */
    "null"                         { return symbol(NULL); }

  /* zen keywords */
    "objState"                     { return symbol(OBJ_STATE); }
    "image"                        { return symbol(IMAGE); }

  /* separators */
    "("                            { return symbol(LPAREN); }
    ")"                            { return symbol(RPAREN); }
    "["                            { return symbol(LBRACK); }
    "]"                            { return symbol(RBRACK); }
    ","                            { return symbol(COMMA); }
    "."                            { return symbol(DOT); }
    "{"                            { return symbol(LCURLY); }
    "}"                            { return symbol(RCURLY); }

  /* string literal */
    "'"                            { yybegin(STRING_SINGLE_QUOTE); string.setLength(0); }
    \"                             { yybegin(STRING_DOUBLE_QUOTE); string.setLength(0); }
    "`"                            { yybegin(STRING_GRAVE_ACCENT); string.setLength(0); }

  /* operators */
    "+"                            { return symbol(PLUS); }
    "-"                            { return symbol(MINUS); }
    "*"                            { return symbol(MULT); }
    "/"                            { return symbol(DIV); }
    "="                            { return symbol(EQ); }
    ">"                            { return symbol(GT); }
    "<"                            { return symbol(LT); }
    "!"                            { return symbol(NOT); }
    "not"                          { return symbol(NOT); }
    "?"                            { return symbol(QUESTION); }
    ":"                            { return symbol(COLON); }
    "=="                           { return symbol(EQEQ); }
    "<="                           { return symbol(LTEQ); }
    ">="                           { return symbol(GTEQ); }
    "!="                           { return symbol(NOTEQ); }
    "<>"                           { return symbol(NOTEQ); }
    "and"                          { return symbol(AND); }
    "&&"                           { return symbol(AND); }
    "or"                           { return symbol(OR); }
    "||"                           { return symbol(OR); }

    {Identifier}                   { return symbol(IDENTIFIER, yytext());}

  /* comments */
    {Comment}                      { /* ignore */ }

  /* whitespace */
    {WhiteSpace}                   { /* ignore */ }
}

<STRING_SINGLE_QUOTE> {
  "'"                            { yybegin(YYINITIAL); return symbol(STRING, string.toString()); }

  {StringSingleQuoteCharacter}+  { string.append( yytext() ); }

  /* escape sequences */
  "\\b"                          { string.append( '\b' ); }
  "\\t"                          { string.append( '\t' ); }
  "\\n"                          { string.append( '\n' ); }
  "\\f"                          { string.append( '\f' ); }
  "\\r"                          { string.append( '\r' ); }
  "\\\""                         { string.append( '\"' ); }
  "\\'"                          { string.append( '\'' ); }
  "\\\\"                         { string.append( '\\' ); }
  \\[0-3]?{OctDigit}?{OctDigit}  { char val = (char) Integer.parseInt(yytext().substring(1),8);
                        				   string.append( val ); }

  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated string at end of line"); }
}

<STRING_DOUBLE_QUOTE> {
  \"                             { yybegin(YYINITIAL); return symbol(STRING, string.toString()); }

  {StringDoubleQuoteCharacter}+  { string.append( yytext() ); }

  /* escape sequences */
  "\\b"                          { string.append( '\b' ); }
  "\\t"                          { string.append( '\t' ); }
  "\\n"                          { string.append( '\n' ); }
  "\\f"                          { string.append( '\f' ); }
  "\\r"                          { string.append( '\r' ); }
  "\\\""                         { string.append( '\"' ); }
  "\\'"                          { string.append( '\'' ); }
  "\\\\"                         { string.append( '\\' ); }
  \\[0-3]?{OctDigit}?{OctDigit}  { char val = (char) Integer.parseInt(yytext().substring(1),8);
                        				   string.append( val ); }

  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated string at end of line"); }
}

<STRING_GRAVE_ACCENT> {
  "`"                            { yybegin(YYINITIAL); return symbol(STRING, string.toString()); }

  {StringGraveAccentCharacter}+  { string.append( yytext() ); }

  /* escape sequences */
  "\\b"                          { string.append( '\b' ); }
  "\\t"                          { string.append( '\t' ); }
  "\\n"                          { string.append( '\n' ); }
  "\\f"                          { string.append( '\f' ); }
  "\\r"                          { string.append( '\r' ); }
  "\\\""                         { string.append( '\"' ); }
  "\\'"                          { string.append( '\'' ); }
  "\\\\"                         { string.append( '\\' ); }
  \\[0-3]?{OctDigit}?{OctDigit}  { char val = (char) Integer.parseInt(yytext().substring(1),8);
                        				   string.append( val ); }

  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated string at end of line"); }
}

/* No token was found for the input so through an error.  Print out an
   Illegal character message with the illegal character that was found. */
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }
