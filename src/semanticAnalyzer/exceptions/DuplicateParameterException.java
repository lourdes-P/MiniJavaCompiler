package semanticAnalyzer.exceptions;

import semanticAnalyzer.symbolTable.*;

public class DuplicateParameterException extends SemanticException {

    public DuplicateParameterException(Method method, Parameter parameter) {
        super("Semantic error in line "+ parameter.getLineNumber() + ": " + method.getName() + "has a duplicate parameter: "+ parameter.getName() + "\n[Error:"+ parameter.getName() + "|" + parameter.getLineNumber() + "]");
    }

}
