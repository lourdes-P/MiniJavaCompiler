package semanticAnalyzer.symbolTable.variables;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.ReferenceType;

public class LocalVariable extends Variable {
    private Method containerMethod;

    public LocalVariable(Token token, ReferenceType type) {
        super(token, type);
    }

    public LocalVariable(Token token, PrimitiveType type) {
        super(token, type);
    }

}
