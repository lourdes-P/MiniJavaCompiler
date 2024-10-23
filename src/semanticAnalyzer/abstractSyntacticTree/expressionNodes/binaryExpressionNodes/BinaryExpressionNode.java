package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;

public abstract class BinaryExpressionNode extends ComposedExpressionNode {
    private ComposedExpressionNode leftSide, rightSide;
    private Token operator;

    public BinaryExpressionNode(ComposedExpressionNode leftSide, ComposedExpressionNode rightSide, Token operator) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operator = operator;
    }

    public BinaryExpressionNode(ComposedExpressionNode leftSide, Token operator) {
        this.leftSide = leftSide;
        this.operator = operator;
    }

    public BinaryExpressionNode(Token operator) {
        this.operator = operator;
    }

    public void setLeftSide(ComposedExpressionNode leftSide) {
        this.leftSide = leftSide;
    }

    public void setRightSide(ComposedExpressionNode rightSide) {
        this.rightSide = rightSide;
    }

    public ComposedExpressionNode getLeftSide() {
        return leftSide;
    }

    public ComposedExpressionNode getRightSide() {
        return rightSide;
    }

    public Token getOperator() {
        return operator;
    }
}
