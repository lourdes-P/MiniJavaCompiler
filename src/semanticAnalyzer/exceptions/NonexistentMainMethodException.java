package semanticAnalyzer.exceptions;

public class NonexistentMainMethodException extends SemanticException {

    public NonexistentMainMethodException() {
        super("No main method declared.\n[Error:|]");
    }
}