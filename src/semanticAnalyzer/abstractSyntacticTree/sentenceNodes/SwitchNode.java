package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes.SwitchSentenceNode;

import java.util.ArrayList;
import java.util.List;

public class SwitchNode extends SentenceNode {
    private ExpressionNode condition;
    private List<SwitchSentenceNode> switchSentenceList;
    private Token switchToken;

    public SwitchNode(Token switchToken) {
        this.switchToken = switchToken;
        condition = null;
        this.switchSentenceList = new ArrayList<>();
    }

    public SwitchNode(ExpressionNode condition) {
        this.condition = condition;
        this.switchSentenceList = new ArrayList<>();
    }

    public void addSwitchSentenceToList(SwitchSentenceNode switchSentenceNode) {
        switchSentenceList.add(switchSentenceNode);
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }
}
