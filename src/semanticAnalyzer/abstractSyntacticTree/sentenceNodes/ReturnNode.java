package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class ReturnNode extends SentenceNode {
    private ExpressionNode returnExpression;

    public ReturnNode(ExpressionNode expressionNode) {
        this.returnExpression = expressionNode;
    }

    public void setReturnExpression(ExpressionNode returnExpression) {
        this.returnExpression = returnExpression;
    }
}
