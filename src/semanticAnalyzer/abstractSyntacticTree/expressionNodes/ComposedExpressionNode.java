package semanticAnalyzer.abstractSyntacticTree.expressionNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes.BinaryExpressionNode;

public abstract class ComposedExpressionNode {
    private ComposedExpressionNode leftSideComposedExpressionNode;
    private BinaryExpressionNode binaryExpressionNode;
    private BasicExpressionNode basicExpressionNode;

    public ComposedExpressionNode(BasicExpressionNode basicExpressionNode) {
        this.basicExpressionNode = basicExpressionNode;
    }

    public ComposedExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode, BinaryExpressionNode binaryExpressionNode, BasicExpressionNode basicExpressionNode) {
        this.leftSideComposedExpressionNode = leftSideComposedExpressionNode;
        this.binaryExpressionNode = binaryExpressionNode;
        this.basicExpressionNode = basicExpressionNode;
    }

    public ComposedExpressionNode getLeftSideComposedExpressionNode() {
        return leftSideComposedExpressionNode;
    }

    public void setLeftSideComposedExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode) {
        this.leftSideComposedExpressionNode = leftSideComposedExpressionNode;
    }

    public BinaryExpressionNode getBinaryExpressionNode() {
        return binaryExpressionNode;
    }

    public void setBinaryExpressionNode(BinaryExpressionNode binaryExpressionNode) {
        this.binaryExpressionNode = binaryExpressionNode;
    }

    public BasicExpressionNode getBasicExpressionNode() {
        return basicExpressionNode;
    }

    public void setBasicExpressionNode(BasicExpressionNode basicExpressionNode) {
        this.basicExpressionNode = basicExpressionNode;
    }
}
