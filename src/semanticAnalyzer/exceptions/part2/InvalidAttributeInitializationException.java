package semanticAnalyzer.exceptions.part2;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidAttributeInitializationException extends SemanticException {

    public InvalidAttributeInitializationException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": initialization of attribute " + token.getLexeme() + " is invalid.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }

    public InvalidAttributeInitializationException(Token token, Token invalidAttribute) {
        super("Semantic error in line " + token.getLineNumber() + ": initialization of attribute " + token.getLexeme() + " is invalid; variable " +invalidAttribute.getLexeme() + " is not yet declared.\n[Error:" + invalidAttribute.getLexeme() + "|" + invalidAttribute.getLineNumber() + "]");
    }
}
