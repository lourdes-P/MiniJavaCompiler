package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public abstract class PrimaryNode {
    private boolean isLeftSideOfAssignment = false, isCallStatement = false;

    public abstract Type statementCheck(SymbolTable symbolTable) throws SemanticException;

    public abstract boolean canBeAssignedAValue();

    public abstract boolean canBeCalled();

    public abstract Token getToken();


    public abstract void generateInterCode(SymbolTable symbolTable, boolean chainIsNull) throws IOException;

    public void setIsLeftSideOfAssignment(boolean b) {
        this.isLeftSideOfAssignment = b;
    }

    public boolean isLeftSideOfAssignment() {
        return isLeftSideOfAssignment;
    }

    public boolean isCallStatement() {
        return isCallStatement;
    }

    public void setIsCallStatement(boolean callStatement) {
        isCallStatement = callStatement;
    }
}
