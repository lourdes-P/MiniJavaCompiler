package semanticAnalyzer.abstractSyntacticTree.expressionNodes;


import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

public class ExpressionNode {
    private ComposedExpressionNode leftSideComposedExpressionNode;

    protected boolean hasRightSide;

    public ExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode) {
        this.leftSideComposedExpressionNode = leftSideComposedExpressionNode;
        hasRightSide = false;
    }

    public ExpressionNode() {
        hasRightSide = false;
    }

    public ComposedExpressionNode getLeftSideComposedExpressionNode() {
        return leftSideComposedExpressionNode;
    }

    public void setLeftSideComposedExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode) {
        this.leftSideComposedExpressionNode = leftSideComposedExpressionNode;
    }

    public boolean hasRightSide() {
        return hasRightSide;
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        return leftSideComposedExpressionNode.statementCheck(symbolTable);
    }

    public void generateInterCode(SymbolTable symbolTable) {
        leftSideComposedExpressionNode.generateInterCode(symbolTable);
    }


    public boolean canBeAssignedAValue() {
        return leftSideComposedExpressionNode.canBeAssignedAValue();
    }

    public boolean canBeCalled() {
        return leftSideComposedExpressionNode.canBeCalled();
    }

}
