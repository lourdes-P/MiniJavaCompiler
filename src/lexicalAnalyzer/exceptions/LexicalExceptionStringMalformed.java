package lexicalAnalyzer.exceptions;

public class LexicalExceptionStringMalformed extends LexicalException {

    public LexicalExceptionStringMalformed(String lexeme, int lineNumber, int column, String line) {
        super("Lexical error in line " + lineNumber + ", column " + column + ": " + lexeme + "; malformed String literal.",lexeme, lineNumber, column, line);
    }

}
