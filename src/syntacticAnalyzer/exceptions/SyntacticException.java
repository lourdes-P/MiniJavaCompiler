package syntacticAnalyzer.exceptions;

import lexicalAnalyzer.Token;

import java.util.List;

public class SyntacticException extends AbstractSyntacticException {

    public SyntacticException(Token currentToken, List<String> expectedTokenNameList) {
        super("Syntactic error in line "+ currentToken.getLineNumber() + ". Expected: " + expectedTokenNameList.toString() + ", encountered: " + currentToken.getLexeme() + "\n[Error:"+ currentToken.getLexeme() + "|" + currentToken.getLineNumber() + "]");
    }
}
