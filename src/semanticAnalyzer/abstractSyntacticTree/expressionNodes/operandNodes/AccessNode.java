package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes;


import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.chainNodes.ChainNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary.PrimaryNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

public class AccessNode extends OperandNode {
    private PrimaryNode primaryNode;
    private ChainNode chainNode;

    public AccessNode() {
        primaryNode = null;
        chainNode = null;
    }

    public AccessNode(PrimaryNode primaryNode) {
        this.primaryNode = primaryNode;
        chainNode = null;
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

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type primaryNodeType = primaryNode.statementCheck(symbolTable);

        if(chainNode == null){
            return primaryNodeType;
        }else{
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
}
