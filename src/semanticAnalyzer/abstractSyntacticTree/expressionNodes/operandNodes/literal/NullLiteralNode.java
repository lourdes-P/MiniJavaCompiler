package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.SymbolTable;

import java.io.IOException;

public class NullLiteralNode extends ObjectLiteralNode {

    public NullLiteralNode(Token nullLiteralToken) {
        super(nullLiteralToken);
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        symbolTable.write("PUSH 0 ; null \n");
    }
}
