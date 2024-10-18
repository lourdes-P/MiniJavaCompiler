package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class CallNode extends SentenceNode {
    private ExpressionNode expressionNode;

    public CallNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }
}
