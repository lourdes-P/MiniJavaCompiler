package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.symbolTable.Block;

public class BlockNode extends SentenceNode {
    private Block correspondingBlock;

    public BlockNode(Block correspondingBlock) {
        this.correspondingBlock = correspondingBlock;
    }
}
