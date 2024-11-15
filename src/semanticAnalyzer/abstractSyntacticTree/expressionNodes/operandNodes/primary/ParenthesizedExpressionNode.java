package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

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

    public Token getToken() {
        return expression.getLeftSideComposedExpressionNode().getToken();
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable, boolean chainIsNull) throws IOException {
        expression.generateInterCode(symbolTable);
    }
}
