package semanticAnalyzer.exceptions;

import semanticAnalyzer.symbolTable.Class;

public class CircularInheritanceException extends SemanticException {

    public CircularInheritanceException(Class class_) {
        super("Semantic error in line "+ class_.getLineNumber() + ": " + class_.getName() + " presents a circular inheritance. " + "\n[Error:"+ class_.getName() + "|" + class_.getLineNumber() + "]");
    }
}
