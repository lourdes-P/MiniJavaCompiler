package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public class BooleanLiteralNode extends PrimitiveLiteralNode {

    public BooleanLiteralNode(Token booleanLiteralToken) {
        super(booleanLiteralToken);
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        symbolTable.write("PUSH " + (literal.getLexeme().equals("false") ? 0 : 1) + " ; literal boolean\n");
    }
}
