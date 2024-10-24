package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

import java.util.List;

public class WhileNode extends SentenceNode {
    private ExpressionNode condition;
    private List<SentenceNode> whileSentence;
    private Token whileToken;

    public WhileNode(Token whileToken) {
        this.whileToken = whileToken;
        condition = null;
        whileSentence = null;
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setWhileSentence(List<SentenceNode> whileSentence) {
        this.whileSentence = whileSentence;
    }
}
