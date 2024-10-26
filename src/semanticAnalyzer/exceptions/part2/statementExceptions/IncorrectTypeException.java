package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class IncorrectTypeException extends SemanticException {

    public IncorrectTypeException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": value assigned to variable " + token.getLexeme() + " is incompatible with its type.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
