package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes;


import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary.PrimaryNode;

public class AccessNode extends OperandNode {
    private PrimaryNode primaryNode;
    private ChainNode chainNode;

    public AccessNode() {
        primaryNode = null;
        chainNode = null;
    }

    public void setPrimaryNode(PrimaryNode primaryNode) {
        this.primaryNode = primaryNode;
    }

    public void setChainNode(ChainNode chainNode) {
        this.chainNode = chainNode;
    }
}
