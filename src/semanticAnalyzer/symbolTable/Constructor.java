package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.DuplicateParameterException;
import semanticAnalyzer.exceptions.InvalidConstructorException;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.type.ReferenceType;
import semanticAnalyzer.symbolTable.type.Type;

import java.util.HashMap;
import java.util.List;

public class Constructor {
    private Token token;
    private HashMap<String,Parameter> parameterTable;
    private Type type;
    private Class containerClass;

    public Constructor(Token token, Class containerClass) throws InvalidConstructorException {
        parameterTable = new HashMap<>();
        this.token = token;
        this.containerClass = containerClass;
        this.type = new ReferenceType(containerClass.getToken());
        if (!token.getLexeme().equals(containerClass.getName()))
            throw new InvalidConstructorException(containerClass, this);
    }

    public void addParameter(Parameter parameter) throws SemanticException {
        if (!parameterAlreadyExists(parameter)) {
            parameterTable.put(parameter.getName(), parameter);
        } else {
            throw new DuplicateParameterException(this, parameter);
        }
    }

    public void addParameterList(List<Parameter> parameterList) throws SemanticException {
        for (Parameter parameter : parameterList) {
            addParameter(parameter);
        }
    }

    private boolean parameterAlreadyExists(Parameter parameter) {
        return parameterTable.containsKey(parameter.getName());
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setContainerClass(Class containerClass) {
        this.containerClass = containerClass;
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


}
