package semanticAnalyzer.exceptions.part2;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidTypeAttributeInitializationException extends SemanticException {

    public InvalidTypeAttributeInitializationException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": type of initialized attribute " + token.getLexeme() + " is incompatible with declared type.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
