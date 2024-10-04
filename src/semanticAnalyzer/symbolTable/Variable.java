package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.type.PrimitiveType;
import semanticAnalyzer.symbolTable.type.ReferenceType;
import semanticAnalyzer.symbolTable.type.Type;

public abstract class Variable {
    private Token token;
    private Type type;
    private boolean typeIsPrimitive;

    protected Variable(Token token, ReferenceType type) {
        this.token = token;
        this.type = type;
        typeIsPrimitive = false;
    }

    protected Variable(Token token, PrimitiveType type) {
        this.token = token;
        this.type = type;
        typeIsPrimitive = true;
    }

    public String getName() {
        return token.getLexeme();
    }

    public Type getType() {
        return type;
    }

    public int getLineNumber() {
        return token.getLineNumber();
    }

    public Token getToken() {
        return token;
    }

    public boolean isTypePrimitive() {
        return typeIsPrimitive;
    }
}
