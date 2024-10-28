package semanticAnalyzer.exceptions.part2.expressionExceptions;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;

public class InvalidActualArgumentException extends SemanticException {

    public InvalidActualArgumentException(Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": actual argument type " + token.getLexeme() + " and formal argument type are incompatible.\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]");
    }

    public InvalidActualArgumentException(Token idMetVar, Token token) {
        super("Semantic error in line " + token.getLineNumber() + ": actual argument type " + token.getLexeme() + " and formal argument type of method " + idMetVar.getLexeme() + " are incompatible.\n[Error:" + idMetVar.getLexeme() + "|" + idMetVar.getLineNumber() + "]");
    }
}
