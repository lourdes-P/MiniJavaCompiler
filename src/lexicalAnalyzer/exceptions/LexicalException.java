package lexicalAnalyzer.exceptions;

import utils.Formatter;

public class LexicalException extends Exception{
    private String lexeme, line;
    private int lineNumber, column;
    public LexicalException(String errorMessage, String lexeme, int lineNumber, int column, String line) {
        super(errorMessage + "\nDetail: "+ line + "\n" + Formatter.blankGenerator((column+8)-1) + "^\n[Error:"+ lexeme +"|"+ lineNumber + "]");
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
