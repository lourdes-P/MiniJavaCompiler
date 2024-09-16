package syntacticAnalyzer.exceptions;

import lexicalAnalyzer.Token;

public class SyntacticException extends Exception {

    public SyntacticException(Token currentToken, String expectedTokenName) {

    }
}
