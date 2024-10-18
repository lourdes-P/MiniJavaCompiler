package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

public class ParenthesizedExpressionNode extends PrimaryNode {
    private ExpressionNode expression;

    public ParenthesizedExpressionNode(ExpressionNode expression) {
        this.expression = expression;
    }

}
