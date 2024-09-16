package lexicalAnalyzer.exceptions;

public class LexicalExceptionCommentMalformed extends LexicalException {
    public LexicalExceptionCommentMalformed(String lexeme, int lineNumber, int column, String line) {
        super("Lexical error in line " + lineNumber + ", column " + column + ": " + lexeme + "; comment is malformed.", lexeme, lineNumber, column, line);
    }
}
