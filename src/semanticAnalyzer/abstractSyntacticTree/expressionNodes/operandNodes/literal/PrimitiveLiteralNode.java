package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public class PrimitiveLiteralNode extends LiteralNode {

    public PrimitiveLiteralNode(Token primitiveLiteral) {
        super(primitiveLiteral);
    }

    @Override
    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        return new PrimitiveType(literal);
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        symbolTable.write("PUSH " + literal.getLexeme() + "\n");
    }

}
