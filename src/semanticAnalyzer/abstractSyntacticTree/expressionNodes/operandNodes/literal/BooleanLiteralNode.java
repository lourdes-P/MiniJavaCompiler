package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;

public class BooleanLiteralNode extends PrimitiveLiteralNode {

    public BooleanLiteralNode(Token booleanLiteralToken) {
        super(booleanLiteralToken);
    }
}
