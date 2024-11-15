package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.IncompatibleBinaryExpressionException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public class EqualsNode extends BinaryExpressionNode {

    public EqualsNode(ComposedExpressionNode leftSide, ComposedExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public EqualsNode(Token operator) {
        super(operator);
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type leftSideType = getLeftSide().statementCheck(symbolTable);
        Type rightSideType = getRightSide().statementCheck(symbolTable);

        if(leftSideType.getIsPrimitive() && rightSideType.getIsPrimitive()) {
            if (!leftSideType.getType().equals(rightSideType.getType())) {
                throw new IncompatibleBinaryExpressionException(this.getOperator());
            } else {
                return new PrimitiveType(new Token("pr_true", "true", rightSideType.getToken().getLineNumber()));
            }
        } else {
            if ((leftSideType.getIsPrimitive() && !rightSideType.getIsPrimitive()) || (!leftSideType.getIsPrimitive() && rightSideType.getIsPrimitive()))
                throw new IncompatibleBinaryExpressionException(this.getOperator());

            if (symbolTable.extendsClass(rightSideType.getToken(), leftSideType.getToken())) {
                return new PrimitiveType(new Token("pr_true", "true", rightSideType.getToken().getLineNumber()));
            } else {
                if (symbolTable.extendsClass(leftSideType.getToken(), rightSideType.getToken())) {
                    return new PrimitiveType(new Token ("pr_true", "true", rightSideType.getToken().getLineNumber()));
                } else {
                    throw new IncompatibleBinaryExpressionException(this.getOperator());
                }
            }
        }
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        //  TODO equalsNode
    }
}
