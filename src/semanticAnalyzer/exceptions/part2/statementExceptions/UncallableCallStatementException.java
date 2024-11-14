package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class UncallableCallStatementException extends SemanticException {

    public UncallableCallStatementException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": not a call statement.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
