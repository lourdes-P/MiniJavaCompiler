package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class WhileNode extends SentenceNode {
    private ExpressionNode condition;
    private SentenceNode whileSentence;


    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setWhileSentence(SentenceNode whileSentence) {
        this.whileSentence = whileSentence;
    }
}
