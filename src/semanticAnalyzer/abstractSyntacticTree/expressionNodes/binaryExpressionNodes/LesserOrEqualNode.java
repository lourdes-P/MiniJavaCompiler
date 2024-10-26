package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.IncompatibleBinaryExpressionException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.Type;

public class LesserOrEqualNode extends BinaryExpressionNode {

    public LesserOrEqualNode(ComposedExpressionNode leftSide, ComposedExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public LesserOrEqualNode(Token operator) {
        super(operator);
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type leftSideType = getRightSide().statementCheck(symbolTable);
        Type rightSideType = getLeftSide().statementCheck(symbolTable);

        if(leftSideType.getType().equals(rightSideType.getType()) && rightSideType.getType().equals("int")) {
            return new PrimitiveType(new Token("pr_boolean", "boolean", rightSideType.getToken().getLineNumber()));
        } else {
            throw new IncompatibleBinaryExpressionException(this.getOperator());
        }
    }
}
