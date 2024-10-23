package semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;

public class MinusNode extends UnaryExpressionNode {

    public MinusNode(OperandNode operand, Token operator) {
        super(operand, operator);
    }

    public MinusNode(Token operator) {
        super(operator);
    }
}
