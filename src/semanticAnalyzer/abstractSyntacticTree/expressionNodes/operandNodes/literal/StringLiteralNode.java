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
        // El siguiente código es para alocar el string en el .heap en vez del .data
        stringLabel = LabelFactory.createNewLabel();
        symbolTable.write("RMEM 1 ; retorno\n" +
                "PUSH " + (literal.getLexeme().length()-1) + " ; celdas para el string\n" +
                "PUSH simple_malloc\n" +
                "CALL\n");

        for (int i = 1; i < literal.getLexeme().length() - 1; i++) {
            symbolTable.write("DUP ; referencia a string\n" +
                    "PUSH '" + literal.getLexeme().charAt(i) + "'\n" +
                    "STOREREF " + i + "\n");
        }

        symbolTable.write("DUP ; ref a string\n" +
                "PUSH 0 ; terminador de string\n" +
                "STOREREF " + (literal.getLexeme().length() -1) + "\n");

//        symbolTable.write(".DATA\n" + stringLabel +
//                ": DW " + literal.getLexeme() + ",0\n" +
//                ".CODE\n");
    }
}
