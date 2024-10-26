package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.ReferenceType;
import semanticAnalyzer.symbolTable.types.Type;

public class ObjectLiteralNode extends LiteralNode {

    public ObjectLiteralNode(Token objectLiteral) {
        super(objectLiteral);
    }

    @Override
    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        return new ReferenceType(literal);
    }

}
