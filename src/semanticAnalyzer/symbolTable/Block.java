package semanticAnalyzer.symbolTable;

import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.BlockNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;
import semanticAnalyzer.symbolTable.variables.LocalVariable;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private Method containerMethod;
    private List<LocalVariable> declaredVariablesInBlock;
    private BlockNode correspondingBlockNode;
    private Block parentBlock;
    private List<SentenceNode> sentenceNodeList;
    // TODO en algun lado hacer un mapeo de bloques... en Method.
    // que se contengan unos a otros como en la clase MapManager y que
    // los flatteen

    public Block(Method containerMethod) {
        this.containerMethod = containerMethod;
        declaredVariablesInBlock = new ArrayList<>();
        sentenceNodeList = new ArrayList<>();
        parentBlock = null;
        correspondingBlockNode = null;
    }

    public Block(Method containerMethod, Block parentBlock) {
        this.containerMethod = containerMethod;
        this.parentBlock = parentBlock;
        declaredVariablesInBlock = new ArrayList<>();
        sentenceNodeList = new ArrayList<>();
        correspondingBlockNode = null;
    }

    public void setCorrespondingBlockNode(BlockNode correspondingBlockNode) {
        this.correspondingBlockNode = correspondingBlockNode;
    }

    public void setParentBlock(Block parentBlock) {
        this.parentBlock = parentBlock;
    }

    public void addSentenceNode(SentenceNode sentenceNode) {
        sentenceNodeList.add(sentenceNode);
    }

    public void addLocalVariable(LocalVariable localVariable) {
        declaredVariablesInBlock.add(localVariable);
        // TODO resolver visibilidad (por "herencia" de bloques)
    }


}
