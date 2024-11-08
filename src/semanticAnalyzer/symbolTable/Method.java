package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.part1.DuplicateParameterException;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Parameter;

import java.util.*;

public class Method {
    private Token token;
    private HashMap<String, Parameter> parameterTable;
    private Type type;
    private Class containerClass;
    private boolean isStatic;
    private List<Block> blockList;
    private Block currentBlock;
    private int offset;

    public Method() {
        parameterTable = new HashMap<>();
        blockList = new ArrayList<>();
        currentBlock = null;
        isStatic = false;
    }

    public Method(boolean isStatic, Token token, Class containerClass, Type type) {
        parameterTable = new HashMap<>();
        this.isStatic = isStatic;
        this.token = token;
        this.containerClass = containerClass;
        this.type = type;
        blockList = new ArrayList<>();
        currentBlock = null;
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
        return method.getName().equals(this.getName()) && equalParameterList(method.getParameterCollection()) && method.getType().equals(this.getType()) && method.getIsStatic() == this.getIsStatic();
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

    public void addBlock(Block block) {
        blockList.add(block);
    }

    public boolean isBlockListEmpty() {
        return blockList.isEmpty();
    }

    public Collection<Parameter> getParameterCollection() {
        return parameterTable.values();
    }

    public List<Parameter> getOrderedParameterList() {
        return orderByParameterPosition(parameterTable.values());
    }

    protected boolean parameterAlreadyExists(Parameter parameter) {
        return parameterTable.containsKey(parameter.getName());
    }

    protected boolean equalParameterList(Collection<Parameter> collection) {
        List<Parameter> thisParameterList = orderByParameterPosition(parameterTable.values());
        boolean equalList = true;
        if(thisParameterList.size() == collection.size()) {
            List<Parameter> parameterList = orderByParameterPosition(collection);
            for (int i=0; i< parameterList.size(); i++) {
                if (!thisParameterList.get(i).equals(parameterList.get(i)))
                    equalList = false;
            }
        } else
            equalList = false;

        return equalList;
    }

    protected List<Parameter> orderByParameterPosition(Collection<Parameter> values) {
        Parameter parameters[] = new Parameter[values.size()];
        if (values.size() > 0) {
            for (Parameter parameter : values) {
                parameters[parameter.getPositionInMethodParameterList()] = parameter;
            }
        }
        return Arrays.stream(parameters).toList();
    }

    public Block getLastAddedBlock() {
        return blockList.getLast();
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(Block currentBlock) {
        this.currentBlock = currentBlock;
    }

    public boolean containsParameter(String parameterName) {
        return parameterTable.containsKey(parameterName);
    }

    public Parameter getParameter(String parameterName) {
        return parameterTable.get(parameterName);
    }

    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        blockList.getFirst().statementCheck(symbolTable);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void checkOffsets() {
        // TODO eliminar este metodo
        for (Block block : blockList) {
            block.checkOffsets();
        }
    }
}
