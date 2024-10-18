package semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;

public class NotNode extends UnaryExpressionNode{

    public NotNode(OperandNode operand, Token operator) {
        super(operand, operator);
    }

    public NotNode(Token operator) {
        super(operator);
    }
}
