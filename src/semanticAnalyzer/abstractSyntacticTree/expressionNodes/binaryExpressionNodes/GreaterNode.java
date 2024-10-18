package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class GreaterNode extends BinaryExpressionNode {

    public GreaterNode(ExpressionNode leftSide, ExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public GreaterNode(Token operator) {
        super(operator);
    }
}
