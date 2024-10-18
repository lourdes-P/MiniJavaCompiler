package semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes;

import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;

public class SwitchDefaultSentenceNode extends SwitchSentenceNode {
    private SentenceNode sentenceNode;

    public void setSentenceNode(SentenceNode sentenceNode) {
        this.sentenceNode = sentenceNode;
    }
}
