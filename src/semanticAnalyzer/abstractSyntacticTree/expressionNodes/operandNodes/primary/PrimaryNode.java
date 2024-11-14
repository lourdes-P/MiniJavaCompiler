package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

public abstract class PrimaryNode {

    public abstract Type statementCheck(SymbolTable symbolTable) throws SemanticException;

    public abstract boolean canBeAssignedAValue();

    public abstract boolean canBeCalled();

    public abstract Token getToken();
}
