package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class LeftSideCannotBeAssignedAValueException extends SemanticException {

    public LeftSideCannotBeAssignedAValueException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": left side of assignment " + token.getLexeme() + " cannot be assigned a value.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
