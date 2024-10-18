package semanticAnalyzer.abstractSyntacticTree.expressionNodes;

import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.assignmentNodes.AssignmentNode;

public abstract class ExpressionNode {
    private ComposedExpressionNode leftSideComposedExpressionNode, rightSideComposedExpressionNode;
    private AssignmentNode assignmentNode;

    public ExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode) {
        this.leftSideComposedExpressionNode = leftSideComposedExpressionNode;
    }

    public ExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode, ComposedExpressionNode rightSideComposedExpressionNode, AssignmentNode assignmentNode) {
        this.leftSideComposedExpressionNode = leftSideComposedExpressionNode;
        this.rightSideComposedExpressionNode = rightSideComposedExpressionNode;
        this.assignmentNode = assignmentNode;
    }

    public void setRightSideComposedExpressionNode(ComposedExpressionNode rightSideComposedExpressionNode) {
        this.rightSideComposedExpressionNode = rightSideComposedExpressionNode;
    }

    public void setAssignmentNode(AssignmentNode assignmentNode) {
        this.assignmentNode = assignmentNode;
    }

    public ComposedExpressionNode getLeftSideComposedExpressionNode() {
        return leftSideComposedExpressionNode;
    }

    public ComposedExpressionNode getRightSideComposedExpressionNode() {
        return rightSideComposedExpressionNode;
    }

    public AssignmentNode getAssignmentNode() {
        return assignmentNode;
    }
}
