package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class DifferentNumberOfArgumentsException extends SemanticException {

    public DifferentNumberOfArgumentsException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": number of formal arguments in method " + token.getLexeme() + " is different from that of actual arguments.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
