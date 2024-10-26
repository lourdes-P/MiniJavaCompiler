package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class UncallableCallStatementException extends SemanticException {

    public UncallableCallStatementException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": expression is not callable.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
