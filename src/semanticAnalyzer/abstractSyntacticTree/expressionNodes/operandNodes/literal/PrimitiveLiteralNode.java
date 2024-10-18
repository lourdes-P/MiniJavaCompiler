package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;

public class PrimitiveLiteralNode extends LiteralNode {

    public PrimitiveLiteralNode(Token primitiveLiteral) {
        super(primitiveLiteral);
    }
}
