package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

import java.util.List;

public class IfNode extends SentenceNode {
    private ExpressionNode condition;
    private List<SentenceNode> body, elseBody;
    private Token ifToken;

    public IfNode(Token ifToken) {
        this.ifToken = ifToken;
        condition = null;
        body = null;
        elseBody = null;
    }

    public IfNode(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setBody(List<SentenceNode> body) {
        this.body = body;
    }

    public void setElseBody(List<SentenceNode> elseBody) {
        this.elseBody = elseBody;
    }
}
