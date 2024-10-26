package semanticAnalyzer.exceptions.part1;

import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.Class;
import semanticAnalyzer.symbolTable.Constructor;

public class InvalidConstructorException extends SemanticException {

    public InvalidConstructorException(Class class_, Constructor constructor) {
        super("Semantic error in line "+ constructor.getLineNumber() + ": constructor name differs from class name " + class_.getName() + "\n[Error:"+ constructor.getName() + "|" + constructor.getLineNumber() + "]");
    }
}
