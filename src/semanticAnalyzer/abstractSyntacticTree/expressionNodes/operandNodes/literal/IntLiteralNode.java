package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;

public class IntLiteralNode extends PrimitiveLiteralNode {

    public IntLiteralNode(Token intLiteralToken) {
        super(intLiteralToken);
    }
}
