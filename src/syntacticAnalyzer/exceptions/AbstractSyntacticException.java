package syntacticAnalyzer.exceptions;

public abstract class AbstractSyntacticException extends Exception {

    public AbstractSyntacticException(String errorMessage) {
        super(errorMessage);
    }

}
