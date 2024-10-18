package semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;

public class SubtractionAssignmentExpressionNode extends AssignmentExpressionNode {

    public SubtractionAssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode) {
        super(leftSideComposedExpressionNode);
    }

    public SubtractionAssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode, ComposedExpressionNode rightSideComposedExpressionNode, Token assignmentToken) {
        super(leftSideComposedExpressionNode, rightSideComposedExpressionNode, assignmentToken);
    }
}
