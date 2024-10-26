package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidWhileConditionTypeException extends SemanticException {

    public InvalidWhileConditionTypeException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": while condition type is not boolean.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
