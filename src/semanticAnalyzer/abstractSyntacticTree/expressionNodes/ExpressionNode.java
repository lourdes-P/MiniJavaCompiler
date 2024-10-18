package semanticAnalyzer.abstractSyntacticTree.expressionNodes;


public abstract class ExpressionNode {
    private ComposedExpressionNode leftSideComposedExpressionNode;

    public ExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode) {
        this.leftSideComposedExpressionNode = leftSideComposedExpressionNode;
    }

    public ComposedExpressionNode getLeftSideComposedExpressionNode() {
        return leftSideComposedExpressionNode;
    }

}
