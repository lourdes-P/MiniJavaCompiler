package semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;

public class AdditionAssignmentExpressionNode extends AssignmentExpressionNode {

    public AdditionAssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode) {
        super(leftSideComposedExpressionNode);
    }

    public AdditionAssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode, ComposedExpressionNode rightSideComposedExpressionNode, Token assignmentToken) {
        super(leftSideComposedExpressionNode, rightSideComposedExpressionNode, assignmentToken);
    }
}
