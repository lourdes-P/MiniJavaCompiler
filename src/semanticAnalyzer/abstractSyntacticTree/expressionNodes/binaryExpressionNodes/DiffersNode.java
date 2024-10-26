package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.Type;

public class DiffersNode extends BinaryExpressionNode {

    public DiffersNode(ComposedExpressionNode leftSide, ComposedExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public DiffersNode(Token operator) {
        super(operator);
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type leftSideType = getRightSide().statementCheck(symbolTable);
        Type rightSideType = getLeftSide().statementCheck(symbolTable);

        if(leftSideType.getIsPrimitive() && rightSideType.getIsPrimitive()) {
            if (leftSideType.getType().equals(rightSideType.getType())) {
                return new PrimitiveType(new Token ("pr_false", "false", rightSideType.getToken().getLineNumber()));
            } else {
                return new PrimitiveType(new Token("pr_true", "true", rightSideType.getToken().getLineNumber()));
            }
        } else {
            if (symbolTable.extendsClass(rightSideType.getToken(), leftSideType.getToken())) {
                return new PrimitiveType(new Token ("pr_false", "false", rightSideType.getToken().getLineNumber()));
            } else {
                return new PrimitiveType(new Token("pr_true", "true", rightSideType.getToken().getLineNumber()));
            }
        }
    }
}
