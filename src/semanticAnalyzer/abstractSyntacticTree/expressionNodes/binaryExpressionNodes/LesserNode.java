package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class LesserNode extends BinaryExpressionNode {

    public LesserNode(ExpressionNode leftSide, ExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public LesserNode(Token operator) {
        super(operator);
    }
}
