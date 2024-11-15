package semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.IncompatibleTypeAssignmentException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.LeftSideCannotBeAssignedAValueException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

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
        getLeftSideComposedExpressionNode().setIsLeftSideOfAssignment(true);
        rightSideComposedExpressionNode.generateInterCode(symbolTable);
        getLeftSideComposedExpressionNode().generateInterCode(symbolTable);
        // el lado izquierdo debe hacer el storeref, por lo que se hace primero el
        // derecho. el izquierdo hara load, luego swap, y luego el storeref con el offset
        // que corresponda.
        // EL CODIGO GENERAR DE UN NODO VARIABLE DEPENDE DE SI ES UN NODO
        // DEL LADO IZQUIERDO DE UNA ASIGNACIÓN O NO     ??? y como se lo digo
        // el codigo a generar en un nodo variable cuando es el lado izquierdo
        // también depende de si tiene encadenados.
    }

}
