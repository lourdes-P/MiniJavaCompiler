package syntacticAnalyzer.exceptions;

import lexicalAnalyzer.Token;

public class NoMatchSyntacticException extends AbstractSyntacticException {

    public NoMatchSyntacticException(Token currentToken, String expectedTokenName) {
        super("Syntactic error in line "+ currentToken.getLineNumber() + ". Expected: " + expectedTokenName + ", encountered: " + currentToken.getLexeme() + "\n[Error:"+ currentToken.getLexeme() + "|" + currentToken.getLineNumber() + "]");
    }

}
