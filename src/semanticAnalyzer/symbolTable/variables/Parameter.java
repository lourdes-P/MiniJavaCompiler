package semanticAnalyzer.symbolTable.variables;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.type.Type;

public class Parameter extends Variable {
    private Method containerMethod;
    private int positionInList;

    public Parameter(Token token, Type type, Method containerMethod, int positionInList) {
        super(token, type);
        this.containerMethod = containerMethod;
        this.positionInList = positionInList;
    }

    public Parameter(Token token, Type type, int positionInList) {
        super(token, type);
        this.positionInList = positionInList;
    }

    public Parameter(Token token, Type type, Method containerMethod) {
        super(token, type);
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

    public boolean equals(Parameter parameter) {
        return parameter.getType().equals(this.getType()) && positionInList == parameter.getPositionInMethodParameterList();
    }
}
