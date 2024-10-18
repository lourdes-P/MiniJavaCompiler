package semanticAnalyzer.abstractSyntacticTree.sentenceNodes.assignmentNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.AccessNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;

public class AssignmentNode extends SentenceNode {
    private ExpressionNode rightSide;
    private AccessNode leftSide;


}
