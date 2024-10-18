package semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal.PrimitiveLiteralNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;

public class SwitchCaseSentenceNode extends SwitchSentenceNode {
    private PrimitiveLiteralNode primitiveLiteralNode;
    private SentenceNode optionalSentence;

    public SwitchCaseSentenceNode(PrimitiveLiteralNode primitiveLiteralNode) {
        this.primitiveLiteralNode = primitiveLiteralNode;
    }

    public void setPrimitiveLiteralNode(PrimitiveLiteralNode primitiveLiteralNode) {
        this.primitiveLiteralNode = primitiveLiteralNode;
    }

    public void setOptionalSentence(SentenceNode optionalSentence) {
        this.optionalSentence = optionalSentence;
    }
}
