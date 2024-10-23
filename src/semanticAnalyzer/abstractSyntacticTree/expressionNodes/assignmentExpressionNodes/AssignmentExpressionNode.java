package semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class AssignmentExpressionNode extends ExpressionNode {
    private ComposedExpressionNode rightSideExpressionNode;
    private Token assignmentToken;


    public AssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode, Token assignmentToken) {
        super(leftSideComposedExpressionNode);
        this.assignmentToken = assignmentToken;
    }

    public AssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode) {
        super(leftSideComposedExpressionNode);
    }

    public AssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode, ComposedExpressionNode rightSideComposedExpressionNode, Token assignmentToken) {
        super(leftSideComposedExpressionNode);
        this.rightSideExpressionNode = rightSideComposedExpressionNode;
        this.hasRightSide = true;
        this.assignmentToken = assignmentToken;
    }


    public ComposedExpressionNode getRightSideExpressionNode() {
        return rightSideExpressionNode;
    }

    public void setRightSideExpressionNode(ComposedExpressionNode rightSideExpressionNode) {
        this.rightSideExpressionNode = rightSideExpressionNode;
        this.hasRightSide = true;
    }

    public Token getAssignmentToken() {
        return assignmentToken;
    }

    public void setAssignmentToken(Token assignmentToken) {
        this.assignmentToken = assignmentToken;
    }
}
