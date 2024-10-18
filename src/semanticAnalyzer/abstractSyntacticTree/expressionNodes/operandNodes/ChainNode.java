package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.symbolTable.types.Type;

import java.util.List;

public class ChainNode {
    private Token idMetVar;
    private List<ExpressionNode> actualArguments;
    private ChainNode furtherChainNode;
    private Type finalType;

    public ChainNode(Token idMetVar, List<ExpressionNode> actualArguments) {
        this.furtherChainNode = null;
        this.idMetVar = idMetVar;
        this.actualArguments = actualArguments;
        this.finalType = null;
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

    public List<ExpressionNode> getActualArguments() {
        return actualArguments;
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    public Type getFinalType() {
        return finalType;
    }

    public void setFinalType(Type finalType) {
        this.finalType = finalType;
    }
}
