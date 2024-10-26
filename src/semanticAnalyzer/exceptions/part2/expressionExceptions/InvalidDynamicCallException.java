package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidDynamicCallException extends SemanticException {

    public InvalidDynamicCallException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": dynamic method call inside static method.\n[Error:"+ token.getLexeme() +"|"+ token.getLineNumber() + "]");
    }
}
