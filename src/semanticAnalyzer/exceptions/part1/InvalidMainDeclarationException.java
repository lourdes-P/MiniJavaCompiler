package semanticAnalyzer.exceptions.part1;

import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.Class;
import semanticAnalyzer.symbolTable.Method;

public class InvalidMainDeclarationException extends SemanticException {

    public InvalidMainDeclarationException(Class class_, Method main) {
        super("Semantic error in line "+ main.getLineNumber() + ": main method is incorrectly declared in class " + class_.getName() + "\n[Error:"+ main.getName() + "|" + main.getLineNumber() + "]");
    }
}
