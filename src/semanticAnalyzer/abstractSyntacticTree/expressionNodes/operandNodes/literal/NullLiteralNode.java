package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;

public class NullLiteralNode extends ObjectLiteralNode {

    public NullLiteralNode(Token nullLiteralToken) {
        super(nullLiteralToken);
    }
}
