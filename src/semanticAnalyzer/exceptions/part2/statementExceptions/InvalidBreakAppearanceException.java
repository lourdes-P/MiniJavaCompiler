package semanticAnalyzer.exceptions.part2.statementExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidBreakAppearanceException extends SemanticException {

    public InvalidBreakAppearanceException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": break statement doesn't correspond to a while or switch statement.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }
}
