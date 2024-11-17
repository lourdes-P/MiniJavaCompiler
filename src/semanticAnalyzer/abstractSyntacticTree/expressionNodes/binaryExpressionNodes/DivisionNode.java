package semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.IncompatibleBinaryExpressionException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public class DivisionNode extends BinaryExpressionNode {

    public DivisionNode(ComposedExpressionNode leftSide, ComposedExpressionNode rightSide, Token operator) {
        super(leftSide, rightSide, operator);
    }

    public DivisionNode(Token operator) {
        super(operator);
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type leftSideType = getLeftSide().statementCheck(symbolTable);
        Type rightSideType = getRightSide().statementCheck(symbolTable);

        if(leftSideType.getType().equals(rightSideType.getType()) && rightSideType.getType().equals("int")) {
            return new PrimitiveType(new Token("pr_int", "int", rightSideType.getToken().getLineNumber()));
        } else {
            throw new IncompatibleBinaryExpressionException(this.getOperator());
        }
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        getLeftSide().generateInterCode(symbolTable);
        getRightSide().generateInterCode(symbolTable);
        symbolTable.write("DIV\n");
    }
}
