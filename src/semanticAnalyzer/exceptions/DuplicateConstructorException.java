package semanticAnalyzer.exceptions;

import semanticAnalyzer.symbolTable.Class;
import semanticAnalyzer.symbolTable.Constructor;
import semanticAnalyzer.symbolTable.Method;

public class DuplicateConstructorException extends SemanticException{

    public DuplicateConstructorException(Class class_, Constructor constructor) {
        super("Semantic error in line "+ constructor.getLineNumber() + ": " + class_.getName() + " has a duplicate constructor." + "\n[Error:"+ constructor.getName() + "|" + constructor.getLineNumber() + "]");
    }
}
