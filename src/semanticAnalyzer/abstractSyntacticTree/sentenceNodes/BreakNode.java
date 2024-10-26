package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.InvalidBreakAppearanceException;
import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.SymbolTable;

public class BreakNode extends SentenceNode {
    private Token breakToken;
    private Block containerBlock;

    public BreakNode(Token breakToken) {
        this.breakToken = breakToken;
    }

    public void setContainerBlock(Block containerBlock) {
        this.containerBlock = containerBlock;
    }

    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        if (!containerBlock.hasNestedWhileOrSwitchStatement())
            throw new InvalidBreakAppearanceException(breakToken);
    }

    @Override
    public boolean isWhileOrSwitchStatement() {
        return false;
    }
}
