package lexicalAnalyzer;

public class Token {
    private String tokenName, lexeme;
    private int lineNumber;

    public Token(String tokenName, String lexeme, int lineNumber) {
        this.tokenName = tokenName;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }


    public String getTokenName() {
        return tokenName;
    }


    public String getLexeme() {
        return lexeme;
    }


    public int getLineNumber() {
        return lineNumber;
    }


}
