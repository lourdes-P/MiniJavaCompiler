package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes;


import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.chainNodes.ChainNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary.PrimaryNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public class AccessNode extends OperandNode {
    private PrimaryNode primaryNode;
    private ChainNode chainNode;
    private boolean isCallStatement;

    public AccessNode() {
        primaryNode = null;
        chainNode = null;
        isCallStatement = false;
    }

    public AccessNode(PrimaryNode primaryNode) {
        this.primaryNode = primaryNode;
        chainNode = null;
        isCallStatement = false;
    }

    public void setPrimaryNode(PrimaryNode primaryNode) {
        this.primaryNode = primaryNode;
    }

    public PrimaryNode getPrimaryNode() {
        return primaryNode;
    }

    public void setChainNode(ChainNode chainNode) {
        this.chainNode = chainNode;
    }

    public void setIsCallStatement(boolean callStatement) {
        isCallStatement = callStatement;
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type primaryNodeType = primaryNode.statementCheck(symbolTable);

        if(chainNode == null){
            return primaryNodeType;
        } else{
            return chainNode.statementCheck(primaryNodeType, symbolTable);
        }
    }

    @Override
    public boolean canBeAssignedAValue() {
        if (chainNode == null)
            return primaryNode.canBeAssignedAValue();
        else
            return chainNode.canBeAssignedAValue();
    }

    @Override
    public boolean canBeCalled() {
        if (chainNode == null)
            return primaryNode.canBeCalled();
        else
            return chainNode.canBeCalled();
    }

    @Override
    public Token getToken() {
        if (chainNode == null) {
            return primaryNode.getToken();
        } else {
            return chainNode.getIdMetVar();
        }
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        primaryNode.setIsLeftSideOfAssignment(this.isLeftSideOfAssignment());
        primaryNode.setIsCallStatement(isCallStatement);
        primaryNode.generateInterCode(symbolTable, chainNode == null);
        if (chainNode != null) {
            chainNode.setIsLeftSideOfAssignment(this.isLeftSideOfAssignment());
            chainNode.setIsCallStatement(isCallStatement);
            chainNode.generateInterCode(symbolTable);
        }
    }
}
