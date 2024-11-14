package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidReturnStatementException extends SemanticException {

    public InvalidReturnStatementException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": method's return type is not void and return expression is empty.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }

    public InvalidReturnStatementException(Token token, Token type) {
        super("Semantic error in line " + token.getLineNumber() + ": local variable's declared type is null.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }

    public InvalidReturnStatementException(Token token, String methodName) {
        super("Semantic error in line " + token.getLineNumber() + ": method " + methodName + "'s return type is void and return expression is not empty.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
