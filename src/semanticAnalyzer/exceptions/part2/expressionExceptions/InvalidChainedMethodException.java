package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidChainedMethodException extends SemanticException {

    public InvalidChainedMethodException(Token token, String className) {
        super("Semantic error in line " + token.getLineNumber() + ": either could not resolve method name " + token.getLexeme() + " in class " + className + ", or " + className + " is not Class type.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
