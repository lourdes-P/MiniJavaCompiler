package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidReturnStatementException extends SemanticException {

    public InvalidReturnStatementException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": method's return type is not null and return expression is empty.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }

    public InvalidReturnStatementException(Token token, Token type) {
        super("Semantic error in line " + token.getLineNumber() + ": local variable's declared type is null.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
