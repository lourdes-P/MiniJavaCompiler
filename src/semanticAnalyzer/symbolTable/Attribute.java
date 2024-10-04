package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.type.PrimitiveType;
import semanticAnalyzer.symbolTable.type.ReferenceType;

public class Attribute extends Variable {
    private Class containerClass;

    public Attribute(Token token, ReferenceType type, Class class_) {
        super(token, type);
        containerClass = class_;
    }

    public Attribute(Token token, PrimitiveType type, Class class_) {
        super(token, type);
        containerClass = class_;
    }

    public Class getContainerClass() {
        return containerClass;
    }



}
