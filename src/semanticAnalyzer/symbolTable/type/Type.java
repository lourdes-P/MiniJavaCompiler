package semanticAnalyzer.symbolTable.type;

import lexicalAnalyzer.Token;

public abstract class Type {
    private Token token;
    private boolean isPrimitive;

    public Type(Token token) {
        this.token = token;
    }

    protected void setIsPrimitive (boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    public boolean getIsPrimitive() {
        return isPrimitive;
    }

    public Token getToken() {
        return token;
    }

    public String getName() {
        return token.getLexeme();
    }

    public boolean equals(Type type) {
        return type.getName().equals(this.getName());
    }
}
