package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class SubtractionNode extends BinaryExpressionNode {

    public SubtractionNode(ExpressionNode leftSide, ExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public SubtractionNode(Token operator) {
        super(operator);
    }

}
