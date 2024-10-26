package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class MethodNotDeclaredException extends SemanticException {

    public MethodNotDeclaredException(Token token) {
        super("Semantic error in line "+ token.getLineNumber() + ": current class does not declare method "+ token.getLexeme() + ".\n[Error:"+ token.getLexeme()+"|"+token.getLineNumber()+"]");
    }

    public MethodNotDeclaredException(Token token, Token containerClass) {
        super("Semantic error in line "+ token.getLineNumber() + ": class " + containerClass.getLexeme() + " does not declare method "+ token.getLexeme() + ".\n[Error:"+ token.getLexeme()+"|"+token.getLineNumber()+"]");
    }
}
