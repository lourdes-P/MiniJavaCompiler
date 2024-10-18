package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes.AssignmentExpressionNode;

public class AssignmentNode extends SentenceNode {
    private AssignmentExpressionNode assignmentExpressionNode;

    public AssignmentExpressionNode getAssignmentExpressionNode() {
        return assignmentExpressionNode;
    }

    public void setAssignmentExpressionNode(AssignmentExpressionNode assignmentExpressionNode) {
        this.assignmentExpressionNode = assignmentExpressionNode;
    }
}
