package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.type.PrimitiveType;
import semanticAnalyzer.symbolTable.type.ReferenceType;
import semanticAnalyzer.symbolTable.type.Type;

public abstract class Variable {
    private Token token;
    private Type type;

    protected Variable (Token token, Type type) {
        this.token = token;
        this.type = type;
    }

    public void prueba( String s, int i) {

    }

    public String getName() {
        return token.getLexeme();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getLineNumber() {
        return token.getLineNumber();
    }

    public Token getToken() {
        return token;
    }

    public boolean isTypePrimitive() {
        return type.getIsPrimitive();
    }
}
