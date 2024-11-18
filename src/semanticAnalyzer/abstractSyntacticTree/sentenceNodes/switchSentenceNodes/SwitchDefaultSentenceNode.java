package semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import utils.LabelFactory;

import java.io.IOException;
import java.util.List;

public class SwitchDefaultSentenceNode extends SwitchSentenceNode {
    private List<SentenceNode> sentenceNode;

    public SwitchDefaultSentenceNode(Token token) {
        super(token);
        this.sentenceNode = null;
    }

    public void setSentenceNode(List<SentenceNode> sentenceNode) {
        this.sentenceNode = sentenceNode;
    }

    @Override
    public void statementCheck(Type conditionType, SymbolTable symbolTable) throws SemanticException {
        for (SentenceNode sentenceNode : sentenceNode) {
            sentenceNode.statementCheck(symbolTable);
        }
    }

    @Override
    public boolean isWhileOrSwitchStatement() {
        return true;
    }

    @Override
    public String generateInterCode(SymbolTable symbolTable, String afterSwitchLabel) throws IOException {
        this.setAfterCaseLabel(LabelFactory.createNewLabel());
        symbolTable.write(getSwitchStatementLabel() + ": NOP ; default\n");
        symbolTable.write("POP ; saco la condicion\n");
        for (SentenceNode sentenceNode1 : sentenceNode) {
            sentenceNode1.generateInterCode(symbolTable);
        }
        symbolTable.write("JUMP " + afterSwitchLabel + "\n");
        // TODO se necesita break ? porque en java termina nomas, no necesita break

        return this.getAfterCaseLabel();
    }
}
