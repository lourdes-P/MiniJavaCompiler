package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.SymbolTable;

public class BlockNode extends SentenceNode {
    private Block correspondingBlock;

    public BlockNode(Block correspondingBlock) {
        this.correspondingBlock = correspondingBlock;
    }

    @Override
    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        for (SentenceNode sentenceNode : correspondingBlock.getSentenceNodeList()) {
            sentenceNode.statementCheck(symbolTable);
        }
    }

}
