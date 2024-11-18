package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes.SwitchDefaultSentenceNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes.SwitchSentenceNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.InvalidSwitchConditionTypeException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import utils.LabelFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SwitchNode extends SentenceNode {
    private ExpressionNode condition;
    private List<SwitchSentenceNode> switchSentenceList;
    private Token switchToken;
    private String afterSwitchLabel;

    public SwitchNode(Token switchToken) {
        this.switchToken = switchToken;
        condition = null;
        this.switchSentenceList = new ArrayList<>();
    }

    public SwitchNode(ExpressionNode condition) {
        this.condition = condition;
        this.switchSentenceList = new ArrayList<>();
    }

    public void addSwitchSentenceToList(SwitchSentenceNode switchSentenceNode) {
        switchSentenceList.add(switchSentenceNode);
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public Token getToken() {
        return switchToken;
    }

    public String getAfterSwitchLabel() {
        return afterSwitchLabel;
    }

    @Override
    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type conditionType = condition.statementCheck(symbolTable);
        if (!conditionType.getType().equals("boolean") && !conditionType.getType().equals("int") && !conditionType.getType().equals("char")) {
            throw new InvalidSwitchConditionTypeException(switchToken);
        }

        for (SwitchSentenceNode switchSentenceNode : switchSentenceList) {
            switchSentenceNode.statementCheck(conditionType, symbolTable);
        }
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        afterSwitchLabel = LabelFactory.createNewLabel();
        String switchCaseLabel = LabelFactory.createNewLabel();

        for (int i = 0 ; i <switchSentenceList.size()-1 ; i++) {
            switchSentenceList.get(i).setSwitchStatementLabel(switchCaseLabel);
            symbolTable.write(switchCaseLabel + ": NOP\n");
            condition.generateInterCode(symbolTable);
            switchSentenceList.get(i).setAfterCaseLabel(switchCaseLabel = LabelFactory.createNewLabel());
            switchSentenceList.get(i).generateInterCode(symbolTable, afterSwitchLabel);
        }

        if (!switchSentenceList.isEmpty()) {
            switchSentenceList.getLast().setSwitchStatementLabel(switchCaseLabel);
            symbolTable.write(switchCaseLabel + ": NOP\n");
            condition.generateInterCode(symbolTable);
            switchSentenceList.getLast().setAfterCaseLabel(afterSwitchLabel);
            switchSentenceList.getLast().generateInterCode(symbolTable, afterSwitchLabel);
        }

        symbolTable.write(afterSwitchLabel + ": NOP\n");
    }
}
