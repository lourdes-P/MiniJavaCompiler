package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.SymbolTable;
import utils.LabelFactory;

import java.io.IOException;

public class StringLiteralNode extends ObjectLiteralNode {
    private String stringLabel;

    public StringLiteralNode(Token stringLiteralToken) {
        super(stringLiteralToken);
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        stringLabel = LabelFactory.createNewLabel();
        symbolTable.write(".DATA\n +" + stringLabel +
                ": DW " + literal.getLexeme() + ",0\n" +
                ".CODE\n");
    }
}
