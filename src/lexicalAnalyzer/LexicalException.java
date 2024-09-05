package lexicalAnalyzer;

import utils.Formatter;

public class LexicalException extends Exception{
    private String lexeme, line;
    private int lineNumber, column;
    public LexicalException(String lexeme, int lineNumber, int column, String line) {
        super("Lexical error in line " + lineNumber + ", column " + column + ": " + lexeme + ".\nDetail: "+ line + "\n" + Formatter.blankGenerator((column+8)-1) + "^\n[Error:"+ lexeme +"|"+ lineNumber + "]");
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.column = column;
        this.line = line;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumn() {
        return column;
    }

    public String getLine() {
        return line;
    }
}
