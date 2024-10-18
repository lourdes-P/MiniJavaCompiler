package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.symbolTable.Block;

import java.util.List;

public class VarAccessNode extends PrimaryNode {
    private Token idMetVar;
    private List<ExpressionNode> actualArguments;
    private Block accessBlock;

    public VarAccessNode(Token idMetVar, List<ExpressionNode> actualArguments) {
        this.idMetVar = idMetVar;
        this.actualArguments = actualArguments;
    }

    public void setAccessBlock(Block accessBlock) {
        this.accessBlock = accessBlock;
    }

    public String getName() {
        return idMetVar.getLexeme();
    }
}
