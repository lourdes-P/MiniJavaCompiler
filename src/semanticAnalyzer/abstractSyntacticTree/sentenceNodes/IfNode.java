package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.InvalidIfConditionTypeException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.util.List;

public class IfNode extends SentenceNode {
    private ExpressionNode condition;
    private List<SentenceNode> body, elseBody;
    private Token ifToken;

    public IfNode(Token ifToken) {
        this.ifToken = ifToken;
        condition = null;
        body = null;
        elseBody = null;
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setBody(List<SentenceNode> body) {
        this.body = body;
    }

    public void setElseBody(List<SentenceNode> elseBody) {
        this.elseBody = elseBody;
    }


    @Override
    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type conditionType = condition.statementCheck(symbolTable);

        if (!conditionType.getType().equals("boolean")) {
            throw new InvalidIfConditionTypeException(ifToken);
        }

        for (SentenceNode sentenceNode : body) {
            sentenceNode.statementCheck(symbolTable);
        }

        for (SentenceNode sentenceNode : elseBody) {
            sentenceNode.statementCheck(symbolTable);
        }
    }

    @Override
    public boolean isWhileOrSwitchStatement() {
        return false;
    }
}
