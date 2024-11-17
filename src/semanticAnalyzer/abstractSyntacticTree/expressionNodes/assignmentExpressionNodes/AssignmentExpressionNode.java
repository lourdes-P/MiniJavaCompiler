package semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.AccessNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal.LiteralNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary.PrimaryNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary.VarAccessNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.IncompatibleTypeAssignmentException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.LeftSideCannotBeAssignedAValueException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Attribute;

import java.io.IOException;

public class AssignmentExpressionNode extends ExpressionNode {
    private ComposedExpressionNode rightSideComposedExpressionNode;
    private Token assignmentToken;


    public AssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode, Token assignmentToken) {
        super(leftSideComposedExpressionNode);
        this.assignmentToken = assignmentToken;
    }

    public AssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode) {
        super(leftSideComposedExpressionNode);
    }

    public AssignmentExpressionNode(ComposedExpressionNode leftSideComposedExpressionNode, ComposedExpressionNode rightSideComposedExpressionNode, Token assignmentToken) {
        super(leftSideComposedExpressionNode);
        this.rightSideComposedExpressionNode = rightSideComposedExpressionNode;
        this.hasRightSide = true;
        this.assignmentToken = assignmentToken;
    }


    public ComposedExpressionNode getRightSideComposedExpressionNode() {
        return rightSideComposedExpressionNode;
    }

    public void setRightSideComposedExpressionNode(ComposedExpressionNode rightSideComposedExpressionNode) {
        this.rightSideComposedExpressionNode = rightSideComposedExpressionNode;
        this.hasRightSide = true;
    }

    public Token getAssignmentToken() {
        return assignmentToken;
    }

    public void setAssignmentToken(Token assignmentToken) {
        this.assignmentToken = assignmentToken;
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type leftSideType =  getLeftSideComposedExpressionNode().statementCheck(symbolTable);
        Type rightSideType = rightSideComposedExpressionNode.statementCheck(symbolTable);

        if (getLeftSideComposedExpressionNode().canBeAssignedAValue()) {
            if (!leftSideType.getName().equals("Object")) {
                if (!rightSideType.getName().equals("null")) {
                    if (!leftSideType.getIsPrimitive() && !leftSideType.getType().equals(rightSideType.getType()) && !symbolTable.extendsClass(rightSideType.getToken(), leftSideType.getToken())) {
                        throw new IncompatibleTypeAssignmentException(assignmentToken);
                    } else if (leftSideType.getIsPrimitive() && !leftSideType.getType().equals(rightSideType.getType())) {
                        throw new IncompatibleTypeAssignmentException(assignmentToken);
                    }
                } else {
                    if(leftSideType.getIsPrimitive()) {
                        throw new IncompatibleTypeAssignmentException(assignmentToken);
                    }
                }
            }
        } else {
            throw new LeftSideCannotBeAssignedAValueException(assignmentToken);
        }
        return leftSideType;
    }

    @Override
    public boolean canBeAssignedAValue() {
        return rightSideComposedExpressionNode.canBeAssignedAValue();
    }

    @Override
    public boolean canBeCalled() {
        return false;
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
//        if (!setStaticAttributeCode()) {
            getLeftSideComposedExpressionNode().setIsLeftSideOfAssignment(true);
            rightSideComposedExpressionNode.generateInterCode(symbolTable);
            getLeftSideComposedExpressionNode().generateInterCode(symbolTable);
//        } else {
//            getLeftSideComposedExpressionNode().setIsLeftSideOfAssignment(true);
//            getLeftSideComposedExpressionNode().generateInterCode(symbolTable);
//            rightSideComposedExpressionNode.generateInterCode(symbolTable);
//        }
        // el lado izquierdo debe hacer el storeref, por lo que se hace primero el
        // derecho. el izquierdo hara load, luego swap, y luego el storeref con el offset
        // que corresponda.
    }

//    protected boolean setStaticAttributeCode() throws IOException {
//        if ((getRightSideComposedExpressionNode() instanceof LiteralNode literalNode) && (getLeftSideComposedExpressionNode() instanceof AccessNode accessNode)) {
//            PrimaryNode primaryNode = accessNode.getPrimaryNode();
//            if (primaryNode instanceof VarAccessNode) {
//                VarAccessNode varAccessNode = (VarAccessNode) primaryNode;
//                if (varAccessNode.getVariable() instanceof Attribute attribute) {
//                    if (attribute.isStatic()) {
//                        literalNode.setStaticAccess(true);
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

}
