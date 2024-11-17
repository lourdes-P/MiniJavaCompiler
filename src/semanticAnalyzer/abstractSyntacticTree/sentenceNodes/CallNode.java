package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.AccessNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.UncallableCallStatementException;
import semanticAnalyzer.symbolTable.SymbolTable;

import java.io.IOException;

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

        expressionNode.statementCheck(symbolTable);
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        ((AccessNode) expressionNode.getLeftSideComposedExpressionNode()).setIsCallStatement(true);
        expressionNode.generateInterCode(symbolTable);
    }

}
