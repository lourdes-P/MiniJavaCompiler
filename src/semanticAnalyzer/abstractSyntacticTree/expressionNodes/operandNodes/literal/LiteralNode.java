package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;

public abstract class LiteralNode extends OperandNode {
    private Token literal;

    public LiteralNode(Token literal) {
        this.literal = literal;
    }
}
