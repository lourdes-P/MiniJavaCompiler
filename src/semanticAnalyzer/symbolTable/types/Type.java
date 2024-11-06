package semanticAnalyzer.symbolTable.types;

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

    public abstract String getType();

    public static Type getType(Token idMetVar) {
        if(idMetVar.getTokenName().equals("pr_boolean"))
            return new PrimitiveType(new Token ("pr_boolean", "boolean",idMetVar.getLineNumber()));
        else if(idMetVar.getTokenName().equals("pr_true"))
            return new PrimitiveType(new Token ("pr_true", "boolean",idMetVar.getLineNumber()));
        else if(idMetVar.getTokenName().equals("pr_false"))
            return new PrimitiveType(new Token ("pr_false", "boolean",idMetVar.getLineNumber()));
        else if(idMetVar.getTokenName().equals("intLiteral"))
            return new PrimitiveType(new Token ("intLiteral", "int",idMetVar.getLineNumber()));
        else if(idMetVar.getTokenName().equals("pr_int"))
            return new PrimitiveType(new Token ("pr_int", "int",idMetVar.getLineNumber()));
        else if(idMetVar.getTokenName().equals("charLiteral"))
            return new PrimitiveType(new Token ("charLiteral", "char",idMetVar.getLineNumber()));
        else if(idMetVar.getTokenName().equals("pr_char"))
           return new PrimitiveType(new Token ("pr_char", "char",idMetVar.getLineNumber()));
        else if(idMetVar.getTokenName().equals("idClase"))
            return new ReferenceType(new Token ("idClase", idMetVar.getLexeme(), idMetVar.getLineNumber()));
        else if (idMetVar.getTokenName().equals("stringLiteral"))
            return new ReferenceType(new Token ("idClase", "String", idMetVar.getLineNumber()));
        else if (idMetVar.getTokenName().equals("pr_null"))
            return new ReferenceType(new Token ("idClase", "null", idMetVar.getLineNumber()));
        else
            return null;
    }
}
