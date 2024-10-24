package semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;

public abstract class SwitchSentenceNode {
    private Token switchSentenceToken;

    public SwitchSentenceNode(Token token) {
        switchSentenceToken = token;
    }

}
