package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public abstract class BinaryExpressionNode {
    private ExpressionNode leftSide, rightSide;
    private Token operator;

    public BinaryExpressionNode(ExpressionNode leftSide, ExpressionNode rightSide, Token operator) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operator = operator;
    }

    public BinaryExpressionNode(Token operator) {
        this.operator = operator;
    }

    public void setLeftSide(ExpressionNode leftSide) {
        this.leftSide = leftSide;
    }

    public void setRightSide(ExpressionNode rightSide) {
        this.rightSide = rightSide;
    }

    public ExpressionNode getLeftSide() {
        return leftSide;
    }

    public ExpressionNode getRightSide() {
        return rightSide;
    }

    public Token getOperator() {
        return operator;
    }
}
