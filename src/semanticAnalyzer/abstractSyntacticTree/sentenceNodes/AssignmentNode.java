package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes.AssignmentExpressionNode;

public class AssignmentNode extends SentenceNode {
    private AssignmentExpressionNode assignmentExpressionNode;

    public AssignmentNode(AssignmentExpressionNode assignmentExpressionNode) {
        this.assignmentExpressionNode = assignmentExpressionNode;
    }

    public void setAssignmentExpressionNode(AssignmentExpressionNode assignmentExpressionNode) {
        this.assignmentExpressionNode = assignmentExpressionNode;
    }
}
