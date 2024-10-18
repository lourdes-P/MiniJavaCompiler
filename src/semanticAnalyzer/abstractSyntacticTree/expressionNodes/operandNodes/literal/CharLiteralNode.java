package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;

public class CharLiteralNode extends PrimitiveLiteralNode {

    public CharLiteralNode(Token charLiteralToken) {
        super(charLiteralToken);
    }
}
