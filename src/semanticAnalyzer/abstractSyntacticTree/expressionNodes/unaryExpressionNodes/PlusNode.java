package semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;

public class PlusNode extends UnaryExpressionNode {

    public PlusNode(OperandNode operand, Token operator) {
        super(operand, operator);
    }

    public PlusNode(Token operator) {
        super(operator);
    }
}
