package semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;

import java.util.List;

public class SwitchDefaultSentenceNode extends SwitchSentenceNode {
    private List<SentenceNode> sentenceNode;

    public SwitchDefaultSentenceNode(Token token) {
        super(token);
        this.sentenceNode = null;
    }

    public void setSentenceNode(List<SentenceNode> sentenceNode) {
        this.sentenceNode = sentenceNode;
    }
}
