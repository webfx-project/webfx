
/* --------------------------Usercode Section------------------------ */

package webfx.platform.shared.services.json.parser.jflex;

import java_cup.runtime.*;
import webfx.platform.shared.services.json.parser.javacup.JsonSymbols;
import static webfx.platform.shared.services.json.parser.javacup.JsonSymbols.*;

%%
   
/* -----------------Options and Declarations Section----------------- */

%public
%class JsonLexer
%line
%column
%cupsym JsonSymbols
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
Identifier = [A-Za-z_][A-Za-z_0-9]*
StringSingleQuoteCharacter = [^\r\n'\\]
StringDoubleQuoteCharacter = [^\r\n\"\\]
StringGraveAccentCharacter = [^\r\n`\\]
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
IntegerLiteral = 0 | [1-9][0-9]*
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?
FLit1    = [0-9]+ \. [0-9]*
FLit2    = \. [0-9]+
FLit3    = [0-9]+
Exponent = [eE] [+-]? [0-9]+

%state STRING_SINGLE_QUOTE, STRING_DOUBLE_QUOTE, STRING_GRAVE_ACCENT

%%
/* ------------------------Lexical Rules Section---------------------- */
   

<YYINITIAL> {

  /* keywords */

  /* internal usage parser tokens */

    {IntegerLiteral}               { return symbol(NUMBER, Integer.valueOf(yytext())); }
    {DoubleLiteral}                { return symbol(NUMBER, Double.valueOf(yytext())); }

  /* boolean literals */
    "true"                         { return symbol(BOOLEAN, Boolean.TRUE); }
    "false"                        { return symbol(BOOLEAN, Boolean.FALSE); }

  /* null literal */
    "null"                         { return symbol(NULL); }

  /* separators */
    "{"                            { return symbol(LCURLY); }
    "}"                            { return symbol(RCURLY); }
    "["                            { return symbol(LBRACK); }
    "]"                            { return symbol(RBRACK); }
    ","                            { return symbol(COMMA); }
    ":"                            { return symbol(COLON); }

  /* string literal */
    "'"                            { yybegin(STRING_SINGLE_QUOTE); string.setLength(0); }
    \"                             { yybegin(STRING_DOUBLE_QUOTE); string.setLength(0); }
    "`"                            { yybegin(STRING_GRAVE_ACCENT); string.setLength(0); }

  /* operators */
    "-"                            { return symbol(MINUS); }

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
  "\\\\"                         { string.append( '\\' ); }
  \\.                            { string.append( yytext() ); }

  /* error cases */
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
  "\\\\"                         { string.append( '\\' ); }
  \\.                            { string.append( yytext() ); }

  /* error cases */
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
  "\\\\"                         { string.append( '\\' ); }
  \\.                            { string.append( yytext() ); }

  /* error cases */
  {LineTerminator}               { throw new RuntimeException("Unterminated string at end of line"); }
}

/* No token was found for the input so through an error.  Print out an
   Illegal character message with the illegal character that was found. */
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }
