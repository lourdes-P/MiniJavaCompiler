package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.DuplicateParameterException;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.type.Type;
import java.util.HashMap;

public class Method {
    private Token token;
    private HashMap<String,Parameter> parameterTable;
    private Type type;
    private Class containerClass;
    private boolean isStatic;

    public Method() {
        parameterTable = new HashMap<>();
        isStatic = false;
    }

    public Method(boolean isStatic, Token token, Class containerClass, Type type) {
        parameterTable = new HashMap<>();
        this.isStatic = isStatic;
        this.token = token;
        this.containerClass = containerClass;
        this.type = type;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setContainerClass(Class containerClass) {
       this.containerClass = containerClass;
    }

    public void setStatic(boolean on) {
        isStatic = on;
    }

    public String getName() {
        return token.getLexeme();
    }

    public int getLineNumber() {
        return token.getLineNumber();
    }

    public Class getContainerClass() {
        return containerClass;
    }

    public Type getType() {
        return type;
    }

    public boolean getIsStatic() {
        return isStatic;
    }

    public boolean equals(Method method) {
        return method.getName().equals(this.getName());
    }

    public void addParameter(Parameter parameter) throws SemanticException {
        if (!parameterAlreadyExists(parameter)) {
            parameterTable.put(parameter.getName(), parameter);
        } else {
            throw new DuplicateParameterException(this, parameter);
        }
    }

    private boolean parameterAlreadyExists(Parameter parameter) {
        return parameterTable.containsKey(parameter.getName());
    }

}
