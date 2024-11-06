package semanticAnalyzer.exceptions.part2;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidAttributeInitialization extends SemanticException {

    public InvalidAttributeInitialization(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": initialization of attribute " + token.getLexeme() + " is invalid.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
