package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.type.PrimitiveType;
import semanticAnalyzer.symbolTable.type.ReferenceType;

public class Parameter extends Variable {
    private Method containerMethod;
    private int positionInList;

    public Parameter(Token token, ReferenceType type, Method containerMethod, int positionInList) {
        super(token, type);
        this.containerMethod = containerMethod;
        this.positionInList = positionInList;
    }

    public Parameter(Token token, PrimitiveType type, Method containerMethod, int positionInList) {
        super(token, type);
        this.containerMethod = containerMethod;
        this.positionInList = positionInList;
    }

    public Parameter(Token token, ReferenceType type, int positionInList) {
        super(token, type);
        this.positionInList = positionInList;
    }

    public Parameter(Token token, PrimitiveType type, int positionInList) {
        super(token, type);
        this.positionInList = positionInList;
    }

    public void setContainerMethod(Method containerMethod) {
        this.containerMethod = containerMethod;
    }

    public Method getContainerMethod() {
        return containerMethod;
    }

    public void setPositionInMethodParameterList(int i) {
        positionInList = i;
    }

    public int getPositionInMethodParameterList() {
        return positionInList;
    }
}
