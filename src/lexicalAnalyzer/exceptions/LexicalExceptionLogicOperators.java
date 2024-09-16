package lexicalAnalyzer.exceptions;

public class LexicalExceptionLogicOperators extends LexicalException {
    public LexicalExceptionLogicOperators(String lexeme, int lineNumber, int column, String line) {
        super("Lexical error in line " + lineNumber + ", column " + column + ": " + lexeme + "; logic operator incomplete.", lexeme, lineNumber, column, line);
    }
}
