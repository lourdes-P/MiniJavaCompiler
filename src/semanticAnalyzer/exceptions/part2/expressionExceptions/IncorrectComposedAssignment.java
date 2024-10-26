package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class IncorrectComposedAssignment extends SemanticException {

    public IncorrectComposedAssignment(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": types on one or both ends of the " + token.getLexeme() + " assignment are not of int type.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
