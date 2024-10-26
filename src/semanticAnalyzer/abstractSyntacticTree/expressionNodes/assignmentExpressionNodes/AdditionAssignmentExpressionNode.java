package semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.IncorrectComposedAssignment;
import semanticAnalyzer.exceptions.part2.expressionExceptions.LeftSideCannotBeAssignedAValueException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

public class AdditionAssignmentExpressionNode extends AssignmentExpressionNode {


    public AdditionAssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode) {
        super(leftSideComposedExpressionNode);
    }

    public AdditionAssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode, Token assignmentToken) {
        super(leftSideComposedExpressionNode, assignmentToken);
    }

    public AdditionAssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode, ComposedExpressionNode rightSideComposedExpressionNode, Token assignmentToken) {
        super(leftSideComposedExpressionNode, rightSideComposedExpressionNode, assignmentToken);
    }

    @Override
    public boolean canBeAssignedAValue() {
        return false;
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type leftSideType =  getLeftSideComposedExpressionNode().statementCheck(symbolTable);
        Type rightSideType = getRightSideComposedExpressionNode().statementCheck(symbolTable);

        if (getLeftSideComposedExpressionNode().canBeAssignedAValue()) {
            if(!(leftSideType.getType().equals(rightSideType.getType()) && rightSideType.getType().equals("int"))) {
                throw new IncorrectComposedAssignment(getAssignmentToken());
            }
        } else {
            throw new LeftSideCannotBeAssignedAValueException(getAssignmentToken());
        }
        return leftSideType;
    }
}
