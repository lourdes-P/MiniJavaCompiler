package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class LesserOrEqualNode extends BinaryExpressionNode {

    public LesserOrEqualNode(ExpressionNode leftSide, ExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public LesserOrEqualNode(Token operator) {
        super(operator);
    }
}
