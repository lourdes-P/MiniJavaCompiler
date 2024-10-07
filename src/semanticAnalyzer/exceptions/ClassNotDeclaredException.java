package semanticAnalyzer.exceptions;

import lexicalAnalyzer.Token;

public class ClassNotDeclaredException extends SemanticException{

    public ClassNotDeclaredException(Token nonexistentClassName) {
        super("Semantic error in line "+ nonexistentClassName.getLineNumber() + ": class" + nonexistentClassName.getLexeme() + "hasn't been declared. " + "\n[Error:"+ nonexistentClassName.getLexeme() + "|" + nonexistentClassName.getLineNumber() + "]");
    }
}
