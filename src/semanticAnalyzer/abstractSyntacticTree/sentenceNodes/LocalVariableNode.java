package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.variables.LocalVariable;

public class LocalVariableNode extends SentenceNode {
    private Token idMetVar;
    private LocalVariable variable;
    private ExpressionNode rightSide;
    private Block containerBlock;

    public LocalVariableNode(Token idMetVar) {
        this.idMetVar = idMetVar;
    }

    public LocalVariableNode(Token idMetVar, Block containerBlock) {
        this.idMetVar = idMetVar;
        this.containerBlock = containerBlock;
    }

    public void setIdMetVar(Token idMetVar) {
        this.idMetVar = idMetVar;
    }

    public void setRightSide(ExpressionNode rightSide) {
        this.rightSide = rightSide;
    }

    public LocalVariable getVariable() {
        return variable;
    }
}
