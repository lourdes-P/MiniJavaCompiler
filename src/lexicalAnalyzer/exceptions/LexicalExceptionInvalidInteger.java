package lexicalAnalyzer.exceptions;

public class LexicalExceptionInvalidInteger extends LexicalException {

    public LexicalExceptionInvalidInteger(String lexeme, int lineNumber, int column, String line) {
        super("Lexical error in line " + lineNumber + ", column " + column + ": " + lexeme + " is not a valid integer literal.", lexeme, lineNumber, column, line);
    }
}
