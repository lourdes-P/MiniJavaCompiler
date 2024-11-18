package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
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
        if (!switchSentenceList.isEmpty())
            switchSentenceList.getFirst().setSwitchStatementLabel(LabelFactory.createNewLabel());

        for (SwitchSentenceNode switchSentenceNode : switchSentenceList) {
            condition.generateInterCode(symbolTable);
            switchSentenceNode.setSwitchStatementLabel(switchSentenceNode.generateInterCode(symbolTable, afterSwitchLabel));
        }
        afterSwitchLabel = switchSentenceList.getLast().getAfterCaseLabel();
        symbolTable.write(afterSwitchLabel + ": NOP\n");
    }
}
