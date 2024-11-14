package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes.SwitchSentenceNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.InvalidSwitchConditionTypeException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.util.ArrayList;
import java.util.List;

public class SwitchNode extends SentenceNode {
    private ExpressionNode condition;
    private List<SwitchSentenceNode> switchSentenceList;
    private Token switchToken;

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
    public boolean isWhileOrSwitchStatement() {
        return true;
    }
}
