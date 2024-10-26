package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidOperandTypeForUnaryOperatorException extends SemanticException {

    public InvalidOperandTypeForUnaryOperatorException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": operand type is invalid for unary operator " + token.getLexeme() + "\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
