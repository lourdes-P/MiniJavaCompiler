package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.UncallableCallStatementException;
import semanticAnalyzer.symbolTable.SymbolTable;

public class CallNode extends SentenceNode {
    private ExpressionNode expressionNode;
    private Token callToken;

    public CallNode(Token callToken, ExpressionNode expressionNode) {
        this.callToken = callToken;
        this.expressionNode = expressionNode;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public void setCallToken(Token callToken) {
        this.callToken = callToken;
    }

    @Override
    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        if (!expressionNode.canBeCalled())
            throw new UncallableCallStatementException(callToken);
    }

    @Override
    public boolean isWhileOrSwitchStatement() {
        return false;
    }
}
