package lexicalAnalyzer.exceptions;

public class LexicalExceptionInvalidSymbol extends LexicalException {
    public LexicalExceptionInvalidSymbol(String lexeme, int lineNumber, int column, String line) {
        super("Lexical error in line " + lineNumber + ", column " + column + ": " + lexeme + " is an invalid symbol.", lexeme, lineNumber, column, line);
    }
}
