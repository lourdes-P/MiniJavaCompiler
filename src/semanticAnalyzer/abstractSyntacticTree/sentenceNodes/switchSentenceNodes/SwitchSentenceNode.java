package semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public abstract class SwitchSentenceNode {
    private Token switchSentenceToken;
    private String switchStatementLabel, afterCaseLabel;

    public SwitchSentenceNode(Token token) {
        switchSentenceToken = token;
    }

    public abstract void statementCheck(Type conditionType, SymbolTable symbolTable) throws SemanticException;

    public Token getSwitchSentenceToken() {
        return switchSentenceToken;
    }

    public String getSwitchStatementLabel() {
        return switchStatementLabel;
    }

    public void setSwitchStatementLabel(String switchStatementLabel) {
        this.switchStatementLabel = switchStatementLabel;
    }

    public String getAfterCaseLabel() {
        return afterCaseLabel;
    }

    public void setAfterCaseLabel(String afterCaseLabel) {
        this.afterCaseLabel = afterCaseLabel;
    }

    public abstract boolean isWhileOrSwitchStatement();

    public abstract String generateInterCode(SymbolTable symbolTable, String afterSwitchLabel) throws IOException;
}
