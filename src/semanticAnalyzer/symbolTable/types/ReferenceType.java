package semanticAnalyzer.symbolTable.types;

import lexicalAnalyzer.Token;

public class ReferenceType extends Type {

    public ReferenceType(Token token) {
        super(token);
        setIsPrimitive(false);
    }

    public String getType() {
        if(this.getToken().getTokenName().equals("idClase"))
            return this.getToken().getLexeme();
        else
            return "null";
    }
}
