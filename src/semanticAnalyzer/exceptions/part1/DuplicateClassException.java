package semanticAnalyzer.exceptions.part1;

import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.Class;

public class DuplicateClassException extends SemanticException {

    public DuplicateClassException(Class class_) {
        super("Semantic error in line "+ class_.getLineNumber() + ": class " + class_.getName() + " has already been declared"+ "\n[Error:"+ class_.getName() + "|" + class_.getLineNumber() + "]");
    }
}
