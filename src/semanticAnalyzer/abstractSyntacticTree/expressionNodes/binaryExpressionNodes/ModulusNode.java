package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class ModulusNode extends BinaryExpressionNode {

    public ModulusNode(ExpressionNode leftSide, ExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public ModulusNode(Token operator) {
        super(operator);
    }
}


