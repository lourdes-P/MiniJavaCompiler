package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class ReturnNode extends SentenceNode {
    private ExpressionNode returnExpression;
    private Token returnToken;

    public ReturnNode(Token returnToken) {
        this.returnToken = returnToken;
    }

    public void setReturnExpression(ExpressionNode returnExpression) {
        this.returnExpression = returnExpression;
    }
}
