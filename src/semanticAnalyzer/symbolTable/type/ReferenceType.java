package semanticAnalyzer.symbolTable.type;

import lexicalAnalyzer.Token;

public class ReferenceType extends Type {

    public ReferenceType(Token token) {
        super(token);
        setIsPrimitive(false);
    }
}
