package semanticAnalyzer.abstractSyntacticTree.expressionNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes.UnaryExpressionNode;

public abstract class BasicExpressionNode {
    private UnaryExpressionNode unaryExpressionNode;
    private OperandNode operandNode;

    public BasicExpressionNode(OperandNode operandNode) {
        this.operandNode = operandNode;
    }

    public BasicExpressionNode(UnaryExpressionNode unaryExpressionNode, OperandNode operandNode) {
        this.unaryExpressionNode = unaryExpressionNode;
        this.operandNode = operandNode;
    }

    public UnaryExpressionNode getUnaryExpressionNode() {
        return unaryExpressionNode;
    }

    public void setUnaryExpressionNode(UnaryExpressionNode unaryExpressionNode) {
        this.unaryExpressionNode = unaryExpressionNode;
    }

    public OperandNode getOperandNode() {
        return operandNode;
    }

    public void setOperandNode(OperandNode operandNode) {
        this.operandNode = operandNode;
    }
}
