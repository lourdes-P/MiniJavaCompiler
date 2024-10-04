package semanticAnalyzer.symbolTable.type;

import lexicalAnalyzer.Token;

public abstract class Type {
    private Token token;

    public Type(Token token) {
        this.token = token;
    }
}
