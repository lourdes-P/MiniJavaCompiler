package semanticAnalyzer.exceptions.part1;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class ClassNotDeclaredException extends SemanticException {

    public ClassNotDeclaredException(Token nonexistentClassName) {
        super("Semantic error in line "+ nonexistentClassName.getLineNumber() + ": class " + nonexistentClassName.getLexeme() + " hasn't been declared. " + "\n[Error:"+ nonexistentClassName.getLexeme() + "|" + nonexistentClassName.getLineNumber() + "]");
    }
}
