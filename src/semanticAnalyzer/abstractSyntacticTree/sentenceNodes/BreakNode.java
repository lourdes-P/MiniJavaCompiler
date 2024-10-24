package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;

public class BreakNode extends SentenceNode {
    private Token breakToken;

    public BreakNode(Token breakToken) {
        this.breakToken = breakToken;
    }
}
