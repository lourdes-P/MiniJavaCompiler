package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.InvalidIfConditionTypeException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import utils.LabelFactory;

import java.io.IOException;
import java.util.List;

public class IfNode extends SentenceNode {
    private ExpressionNode condition;
    private List<SentenceNode> body, elseBody;
    private Token ifToken;
    private String elseLabel;

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
        if (elseBody != null) {
            for (SentenceNode sentenceNode : elseBody) {
                sentenceNode.statementCheck(symbolTable);
            }
        }
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        condition.generateInterCode(symbolTable);
        elseLabel = LabelFactory.createNewLabel();

        symbolTable.write("BF " + elseLabel + "\n");

        for (SentenceNode sentenceNode : body) {
            sentenceNode.generateInterCode(symbolTable);
        }

        if (elseBody == null) {
            symbolTable.write(elseLabel + ": NOP\n");
        } else {
            symbolTable.write(elseLabel + ": ");
            for (SentenceNode sentenceNode : elseBody) {
                sentenceNode.generateInterCode(symbolTable);
            }
        }
    }
}
