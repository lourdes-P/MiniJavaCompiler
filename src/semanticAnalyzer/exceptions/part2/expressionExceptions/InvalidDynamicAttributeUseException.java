package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidDynamicAttributeUseException extends SemanticException {

    public InvalidDynamicAttributeUseException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": dynamic attribute used inside static method.\n[Error:"+ token.getLexeme() +"|"+ token.getLineNumber() + "]");
    }
}
