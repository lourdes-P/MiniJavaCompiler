package semanticAnalyzer.symbolTable.variables;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.types.Type;

public class LocalVariable extends Variable {

    public LocalVariable(Token token, Type type) {
        super(token, type);
    }

    public LocalVariable(Token token) {
        super(token);
    }
}
