package semanticAnalyzer.exceptions.part1;

import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.Class;

public class DuplicateMethodException extends SemanticException {

    public DuplicateMethodException(Class class_, Method method) {
        super("Semantic error in line "+ method.getLineNumber() + ": " + class_.getName() + " has a duplicate method: "+ method.getName() + "\n[Error:"+ method.getName() + "|" + method.getLineNumber() + "]");
    }
}
