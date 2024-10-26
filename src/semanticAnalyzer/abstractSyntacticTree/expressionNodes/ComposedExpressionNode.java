package semanticAnalyzer.abstractSyntacticTree.expressionNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes.BinaryExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

public abstract class ComposedExpressionNode {

    public abstract Type statementCheck(SymbolTable symbolTable) throws SemanticException;

    public abstract boolean canBeAssignedAValue();

    public abstract boolean canBeCalled();

}
