package lexicalAnalyzer;

public class Token implements TokenInterface {
    private String tokenName, lexeme;
    private int lineNumber, columnNumber;

    public Token(String tokenName, String lexeme, int lineNumber) {
        this.tokenName = tokenName;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    public Token(String tokenName, String lexeme, int lineNumber, int columnNumber) {
        this.tokenName = tokenName;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    @Override
    public String getTokenName() {
        return tokenName;
    }

    @Override
    public String getLexeme() {
        return lexeme;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public int getColumnNumber() {
        return columnNumber;
    }
}
