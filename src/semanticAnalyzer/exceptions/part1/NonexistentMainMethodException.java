package semanticAnalyzer.exceptions.part1;

import semanticAnalyzer.exceptions.SemanticException;

public class NonexistentMainMethodException extends SemanticException {

    public NonexistentMainMethodException() {
        super("No main method declared.\n[Error:|]");
    }
}