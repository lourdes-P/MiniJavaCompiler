package lexicalAnalyzer.exceptions;

public class LexicalExceptionCharacterMalformed extends LexicalException {

    public LexicalExceptionCharacterMalformed(String lexeme, int lineNumber, int column, String line) {
        super("Lexical error in line " + lineNumber + ", column " + column + ": " + lexeme + "; character literal malformed.", lexeme, lineNumber, column, line);
    }
}
