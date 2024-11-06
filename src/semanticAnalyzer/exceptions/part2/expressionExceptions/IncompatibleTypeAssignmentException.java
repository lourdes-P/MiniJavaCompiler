package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class IncompatibleTypeAssignmentException extends SemanticException {

    public IncompatibleTypeAssignmentException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": types on both ends of the assignment " + token.getLexeme() + " are incompatible.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
