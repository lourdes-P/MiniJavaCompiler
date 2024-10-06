package semanticAnalyzer.symbolTable.type;

import lexicalAnalyzer.Token;

public class PrimitiveType extends Type {

    public PrimitiveType(Token token) {
        super(token);
        setIsPrimitive(true);
    }
}
