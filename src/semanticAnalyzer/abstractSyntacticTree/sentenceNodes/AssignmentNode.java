package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes.AssignmentExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;

public class AssignmentNode extends SentenceNode {
    private AssignmentExpressionNode assignmentExpressionNode;

    public AssignmentNode(AssignmentExpressionNode assignmentExpressionNode) {
        this.assignmentExpressionNode = assignmentExpressionNode;
    }

    public void setAssignmentExpressionNode(AssignmentExpressionNode assignmentExpressionNode) {
        this.assignmentExpressionNode = assignmentExpressionNode;
    }

    @Override
    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        assignmentExpressionNode.statementCheck(symbolTable);
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) {
        assignmentExpressionNode.generateInterCode(symbolTable);
    }
}
