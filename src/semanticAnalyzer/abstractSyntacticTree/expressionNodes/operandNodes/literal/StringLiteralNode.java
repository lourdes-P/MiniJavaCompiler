package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;

public class StringLiteralNode extends ObjectLiteralNode {

    public StringLiteralNode(Token stringLiteralToken) {
        super(stringLiteralToken);
    }
}
