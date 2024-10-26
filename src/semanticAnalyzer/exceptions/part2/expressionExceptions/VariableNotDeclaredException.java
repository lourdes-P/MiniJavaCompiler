package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class VariableNotDeclaredException extends SemanticException {

    public VariableNotDeclaredException(Token token) {
        super("Semantic error in line "+ token.getLineNumber() + ": variable named " + token.getLexeme() + " has not been declared.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber());
    }
}
