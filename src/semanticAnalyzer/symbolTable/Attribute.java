package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.type.PrimitiveType;
import semanticAnalyzer.symbolTable.type.ReferenceType;
import semanticAnalyzer.symbolTable.type.Type;

public class Attribute extends Variable {
    private Class containerClass;
    private boolean isStatic;


    public Attribute(Token token, Type type, Class class_, boolean isStatic) {
        super(token, type);
        containerClass = class_;
    }

    public Class getContainerClass() {
        return containerClass;
    }

    public void setStatic(boolean on) {
        isStatic = on;
    }

    public boolean isStatic() {
        return isStatic;
    }


}
