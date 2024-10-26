package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidStaticCallException extends SemanticException {

    public InvalidStaticCallException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": static method call with dynamic method.\n[Error:"+ token.getLexeme() +"|"+ token.getLineNumber() + "]");
    }

}
