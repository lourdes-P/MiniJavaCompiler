package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class NullDeclaredLocalVariableException extends SemanticException {

    public NullDeclaredLocalVariableException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": local variable's declared type is null.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
