package semanticAnalyzer.symbolTable.variables;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.types.Type;

public abstract class Variable {
    private Token token;
    private Type type;
    private boolean invisibleToContainer;

    protected Variable (Token token, Type type) {
        this.token = token;
        this.type = type;
        invisibleToContainer = false;
    }

    public Variable(Token token) {
        this.token = token;
        type = null;
        invisibleToContainer = false;
    }

    public String getName() {
        return token.getLexeme();
    }

    public Type getType() {
        return type;
    }

    public boolean getInvisibleToContainer() {
        return invisibleToContainer;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setInvisibleToContainer(boolean invisible) {
        invisibleToContainer = invisible;
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

    public boolean typeIsResolved() {
        return type != null;
    }
}


