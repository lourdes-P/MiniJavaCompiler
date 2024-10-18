package semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;

public abstract class UnaryExpressionNode {
    private OperandNode operand;
    private Token operator;

    public UnaryExpressionNode(OperandNode operand, Token operator) {
        this.operand = operand;
        this.operator = operator;
    }

}
