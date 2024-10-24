package semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal.PrimitiveLiteralNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;

import java.util.List;

public class SwitchCaseSentenceNode extends SwitchSentenceNode {
    private PrimitiveLiteralNode primitiveLiteralNode;
    private List<SentenceNode> optionalSentence;


    public SwitchCaseSentenceNode(Token switchCaseToken) {
        super(switchCaseToken);
        primitiveLiteralNode = null;
        optionalSentence = null;
    }

    public void setPrimitiveLiteralNode(PrimitiveLiteralNode primitiveLiteralNode) {
        this.primitiveLiteralNode = primitiveLiteralNode;
    }

    public void setOptionalSentence(List<SentenceNode> optionalSentence) {
        this.optionalSentence = optionalSentence;
    }
}
