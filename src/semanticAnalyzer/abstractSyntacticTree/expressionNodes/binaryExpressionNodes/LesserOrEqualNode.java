package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.IncompatibleBinaryExpressionException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.IncompatibleWithRelationalOperatorException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public class LesserOrEqualNode extends BinaryExpressionNode {

    public LesserOrEqualNode(ComposedExpressionNode leftSide, ComposedExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public LesserOrEqualNode(Token operator) {
        super(operator);
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type leftSideType = getLeftSide().statementCheck(symbolTable);
        Type rightSideType = getRightSide().statementCheck(symbolTable);

        if(leftSideType.getType().equals(rightSideType.getType()) && rightSideType.getType().equals("int")) {
            return new PrimitiveType(new Token("pr_boolean", "boolean", rightSideType.getToken().getLineNumber()));
        } else if (!leftSideType.getType().equals("int") || !rightSideType.getType().equals("int")) {
            throw new IncompatibleWithRelationalOperatorException(this.getOperator());
        } else {
            throw new IncompatibleBinaryExpressionException(this.getOperator());
        }
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        //  TODO lesserOrEqualNode
    }
}
