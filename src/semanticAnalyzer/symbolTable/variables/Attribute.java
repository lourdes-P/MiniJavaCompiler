package semanticAnalyzer.symbolTable.variables;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.Class;
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

    public static Attribute clone(Attribute attribute){
        return new Attribute(attribute.getToken(), attribute.getType(), attribute.getContainerClass(), attribute.isStatic());
    }

    public boolean equals(Attribute attribute){
        if(this.getType().equals(attribute.getType()) && this.getName().equals(attribute.getName())){
            return true;
        }else{
            return false;
        }
    }


}
