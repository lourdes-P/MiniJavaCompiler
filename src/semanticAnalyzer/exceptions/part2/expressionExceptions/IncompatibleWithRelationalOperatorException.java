package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class IncompatibleWithRelationalOperatorException extends SemanticException {

    public IncompatibleWithRelationalOperatorException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": one or both types on ends of the relational operator " + token.getLexeme() + " are not of type int.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
