package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.chainNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

public abstract class ChainNode {
    private Token idMetVar;
    private ChainNode furtherChainNode;
    private Type finalType;
    private Method containerMethod;


    public ChainNode() {
        this.furtherChainNode = null;
        this.finalType = null;
        this.idMetVar = null;
        this.containerMethod = null;
    }

    public ChainNode(Token idMetVar) {
        this.idMetVar = idMetVar;
        this.furtherChainNode = null;
        this.finalType = null;
        this.containerMethod = null;
    }

    public ChainNode getFurtherChainNode() {
        return furtherChainNode;
    }

    public void setFurtherChainNode(ChainNode furtherChainNode) {
        this.furtherChainNode = furtherChainNode;
    }

    public Token getIdMetVar() {
        return idMetVar;
    }

    public void setIdMetVar(Token idMetVar) {
        this.idMetVar = idMetVar;
    }

    public void setContainerMethod(Method containerMethod) {
        this.containerMethod = containerMethod;
    }

    public Method getContainerMethod() {
        return containerMethod;
    }

    public Type getFinalType() {
        return finalType;
    }

    public void setFinalType(Type finalType) {
        this.finalType = finalType;
    }

    public abstract Type statementCheck(Type primaryNodeType, SymbolTable symbolTable) throws SemanticException;

    public abstract boolean canBeAssignedAValue();

    public boolean canBeCalled() {
        return !canBeAssignedAValue();
    }

}
