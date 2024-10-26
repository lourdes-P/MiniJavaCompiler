package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.Type;

public class PrimitiveLiteralNode extends LiteralNode {

    public PrimitiveLiteralNode(Token primitiveLiteral) {
        super(primitiveLiteral);
    }

    @Override
    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        return new PrimitiveType(literal);
    }

}
