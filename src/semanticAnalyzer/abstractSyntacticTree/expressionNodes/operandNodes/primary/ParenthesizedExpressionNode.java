package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

public class ParenthesizedExpressionNode extends PrimaryNode {
    private ExpressionNode expression;

    public ParenthesizedExpressionNode(ExpressionNode expression) {
        this.expression = expression;
    }


    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        return expression.statementCheck(symbolTable);
    }

    public boolean canBeAssignedAValue() {
        return expression.canBeAssignedAValue();
    }

    @Override
    public boolean canBeCalled() {
        return false;
    }
}
