package semanticAnalyzer.exceptions.part1;

import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.Method;

public class DuplicateMainException extends SemanticException {

    public DuplicateMainException(Method method) {
        super("Semantic error in line "+ method.getLineNumber() + ": method main() has already been declared"+ "\n[Error:"+ method.getName() + "|" + method.getLineNumber() + "]");
    }
}
