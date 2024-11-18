package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.InvalidBreakAppearanceException;
import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.SymbolTable;

import java.io.IOException;

public class BreakNode extends SentenceNode {
    private Token breakToken;
    private Block containerBlock;
    private WhileNode containerWhileStatement;
    private SwitchNode containerSwitchStatement;

    public BreakNode(Token breakToken) {
        this.breakToken = breakToken;
        containerSwitchStatement = null;
        containerWhileStatement = null;
    }

    public void setContainerBlock(Block containerBlock) {
        this.containerBlock = containerBlock;
    }

    public void setContainerWhileStatement(WhileNode containerWhileStatement) {
        this.containerWhileStatement = containerWhileStatement;
    }

    public void setContainerSwitchStatement(SwitchNode containerSwitchStatement) {
        this.containerSwitchStatement = containerSwitchStatement;
    }

    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        if (containerWhileStatement == null && containerSwitchStatement == null)
            throw new InvalidBreakAppearanceException(breakToken);
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        // TODO breakNode
        if (containerWhileStatement != null)
            symbolTable.write("JUMP " + containerWhileStatement.getAfterWhileLabel() + "\n");

        if (containerSwitchStatement != null)
            symbolTable.write("JUMP " + containerSwitchStatement.getAfterSwitchLabel() + "\n");
    }



}
