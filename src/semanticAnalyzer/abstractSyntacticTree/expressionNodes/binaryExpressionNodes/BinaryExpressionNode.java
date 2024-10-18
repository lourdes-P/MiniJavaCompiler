package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public abstract class BinaryExpressionNode {
    private ExpressionNode leftSide, rightSide;
    private Token operator;

    public BinaryExpressionNode(ExpressionNode leftSide, ExpressionNode rightSide, Token operator) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operator = operator;
    }



}
