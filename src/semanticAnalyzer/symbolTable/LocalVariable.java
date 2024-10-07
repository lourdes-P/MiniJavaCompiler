package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.type.PrimitiveType;
import semanticAnalyzer.symbolTable.type.ReferenceType;

public class LocalVariable extends Variable {
    private Method containerMethod;

    public LocalVariable(Token token, ReferenceType type) {
        super(token, type);
    }

    public LocalVariable(Token token, PrimitiveType type) {
        super(token, type);
    }

}
