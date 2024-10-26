package semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;

public abstract class UnaryExpressionNode extends ComposedExpressionNode {
    private OperandNode operand;
    private Token operator;

    public UnaryExpressionNode(OperandNode operand, Token operator) {
        this.operand = operand;
        this.operator = operator;
    }

    public UnaryExpressionNode(Token operator) {
        this.operator = operator;
    }

    public void setOperandNode(OperandNode operandNode) {
        this.operand = operandNode;
    }

    public OperandNode getOperandNode() {
        return operand;
    }

    public Token getOperatorToken() {
        return operator;
    }

    @Override
    public boolean canBeAssignedAValue() {
        return false;
    }

    @Override
    public boolean canBeCalled() {
        return false;
    }
}
