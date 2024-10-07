package semanticAnalyzer.exceptions;

import semanticAnalyzer.symbolTable.Class;
import semanticAnalyzer.symbolTable.Method;

public class InvalidMethodOverrideException extends SemanticException {

    public InvalidMethodOverrideException(Class class_, Method method) {
        super("Semantic error in line "+ method.getLineNumber() + ": " + class_.getName() + " incorrectly overrides ancestor method "+ method.getName() + "\n[Error:"+ method.getName() + "|" + method.getLineNumber() + "]");
    }
}
