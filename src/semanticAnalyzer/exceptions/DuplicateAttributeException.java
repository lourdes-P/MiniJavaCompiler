package semanticAnalyzer.exceptions;

import semanticAnalyzer.symbolTable.Class;
import semanticAnalyzer.symbolTable.variables.Attribute;

public class DuplicateAttributeException extends SemanticException {

    public DuplicateAttributeException(Class class_, Attribute attribute) {
        super("Semantic error in line "+ attribute.getLineNumber() + ": " + class_.getName() + " has a duplicate attribute: "+ attribute.getName() + "\n[Error:"+ attribute.getName() + "|" + attribute.getLineNumber() + "]");
    }
}
