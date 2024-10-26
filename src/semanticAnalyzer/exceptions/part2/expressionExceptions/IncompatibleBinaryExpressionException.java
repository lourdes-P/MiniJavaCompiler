package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class IncompatibleBinaryExpressionException extends SemanticException {

    public IncompatibleBinaryExpressionException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": types on both ends of the binary operation " + token.getLexeme() + " are incompatible.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
