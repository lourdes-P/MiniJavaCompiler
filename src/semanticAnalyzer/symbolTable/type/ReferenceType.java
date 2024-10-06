package semanticAnalyzer.symbolTable.type;

import lexicalAnalyzer.Token;

public class ReferenceType extends Type {

    // TODO cambiar token por class?
    public ReferenceType(Token token) {
        super(token);
        setIsPrimitive(false);
    }
}
