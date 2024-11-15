package semanticAnalyzer.abstractSyntacticTree.expressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public abstract class ComposedExpressionNode {
    private boolean isLeftSideOfAssignment = false;

    public abstract Type statementCheck(SymbolTable symbolTable) throws SemanticException;

    public abstract boolean canBeAssignedAValue();

    public abstract boolean canBeCalled();

    public abstract Token getToken();

    public abstract void generateInterCode(SymbolTable symbolTable) throws IOException;

    public void setIsLeftSideOfAssignment(boolean b) {
        this.isLeftSideOfAssignment = b;
    }

    public boolean isLeftSideOfAssignment() {
        return isLeftSideOfAssignment;
    }
}
