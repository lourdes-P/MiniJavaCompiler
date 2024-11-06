package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.IncompatibleBinaryExpressionException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.Type;

public class OrNode extends BinaryExpressionNode {

    public OrNode(ComposedExpressionNode leftSide, ComposedExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public OrNode(Token operator) {
        super(operator);
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type leftSideType = getLeftSide().statementCheck(symbolTable);
        Type rightSideType = getRightSide().statementCheck(symbolTable);

        if(leftSideType.getType().equals(rightSideType.getType()) && rightSideType.getType().equals("boolean")) {
            return new PrimitiveType(new Token("pr_boolean", "boolean", rightSideType.getToken().getLineNumber()));
        } else {
            throw new IncompatibleBinaryExpressionException(this.getOperator());
        }
    }
}
