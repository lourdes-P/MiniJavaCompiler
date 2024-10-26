package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidDynamicThisUseException extends SemanticException {

    public InvalidDynamicThisUseException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": dynamic this used inside static method.\n[Error:"+ token.getLexeme() +"|"+ token.getLineNumber() + "]");
    }
}
