package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class DuplicateLocalVariableNameException extends SemanticException {

    public DuplicateLocalVariableNameException(Token token) {
        super("Semantic error in line "+ token.getLineNumber() + ": a local variable named " + token.getLexeme() + " has already been declared.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber()+ "]");
    }
}
