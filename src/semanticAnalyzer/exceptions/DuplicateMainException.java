package semanticAnalyzer.exceptions;

import semanticAnalyzer.symbolTable.Method;

public class DuplicateMainException extends SemanticException {

    public DuplicateMainException(Method method) {
        super("Semantic error in line "+ method.getLineNumber() + ": method main() has already been declared in another class"+ "\n[Error:"+ method.getName() + "|" + method.getLineNumber() + "]");
    }
}
