package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;

public abstract class LiteralNode extends OperandNode {
    protected Token literal;
    protected boolean staticAccess;

    public LiteralNode(Token literal) {
        this.literal = literal;
        staticAccess = false;
    }

    @Override
    public boolean canBeCalled() {
        return false;
    }

    @Override
    public boolean canBeAssignedAValue() {
        return false;
    }

    @Override
    public Token getToken() {
         return literal;
    }

    public boolean isStaticAccess() {
        return staticAccess;
    }

    public void setStaticAccess(boolean staticAccess) {
        this.staticAccess = staticAccess;
    }
}
