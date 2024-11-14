package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class VoidDeclaredAttributeException extends SemanticException {

    public VoidDeclaredAttributeException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": declared type of attribute " + token.getLexeme() + " is void.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");

    }
}
