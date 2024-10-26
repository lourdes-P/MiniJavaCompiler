package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidSwitchConditionTypeException extends SemanticException {

    public InvalidSwitchConditionTypeException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": switch condition type is not of type boolean, int, or char.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }

    public InvalidSwitchConditionTypeException(Token token, String conditionType) {
        super("Semantic error in line " + token.getLineNumber() + ": switch case literal type (" +  token.getLexeme() + ") is not the switch's condition type, " + conditionType + ".\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
