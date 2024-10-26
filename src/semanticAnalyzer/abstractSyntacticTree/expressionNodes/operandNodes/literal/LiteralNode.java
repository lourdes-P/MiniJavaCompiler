package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;

public abstract class LiteralNode extends OperandNode {
    protected Token literal;

    public LiteralNode(Token literal) {
        this.literal = literal;
    }

    @Override
    public boolean canBeCalled() {
        return false;
    }

    @Override
    public boolean canBeAssignedAValue() {
        return false;
    }
}
