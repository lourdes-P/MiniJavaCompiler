package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class IfNode extends SentenceNode {
    private ExpressionNode condition;
    private SentenceNode body, elseBody;

    public IfNode(ExpressionNode condition) {
        this.condition = condition;
    }


    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setBody(SentenceNode body) {
        this.body = body;
    }

    public void setElseBody(SentenceNode elseBody) {
        this.elseBody = elseBody;
    }
}
