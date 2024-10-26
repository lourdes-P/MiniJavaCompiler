package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidIfConditionTypeException extends SemanticException {

    public InvalidIfConditionTypeException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": if condition type is not boolean.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }

}
