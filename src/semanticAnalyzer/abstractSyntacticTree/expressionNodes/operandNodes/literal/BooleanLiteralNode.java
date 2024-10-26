package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

public class BooleanLiteralNode extends PrimitiveLiteralNode {

    public BooleanLiteralNode(Token booleanLiteralToken) {
        super(booleanLiteralToken);
    }

}
