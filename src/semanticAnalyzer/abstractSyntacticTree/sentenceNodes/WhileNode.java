package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.InvalidWhileConditionTypeException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import utils.LabelFactory;

import java.io.IOException;
import java.util.List;

public class WhileNode extends SentenceNode {
    private ExpressionNode condition;
    private List<SentenceNode> whileSentence;
    private Token whileToken;
    private String whileLabel, afterWhileLabel;

    public WhileNode(Token whileToken) {
        this.whileToken = whileToken;
        condition = null;
        whileSentence = null;
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setWhileSentence(List<SentenceNode> whileSentence) {
        this.whileSentence = whileSentence;
    }

    public Token getToken() {
        return whileToken;
    }

    public String getAfterWhileLabel() {
        return afterWhileLabel;
    }

    @Override
    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type whileType = condition.statementCheck(symbolTable);
        if (!whileType.getType().equals("boolean"))
            throw new InvalidWhileConditionTypeException(whileToken);

        for (SentenceNode sentenceNode : whileSentence) {
            sentenceNode.statementCheck(symbolTable);
        }
    }
    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        whileLabel = LabelFactory.createNewLabel();
        afterWhileLabel = LabelFactory.createNewLabel();
        symbolTable.write(whileLabel + ": NOP\n");
        condition.generateInterCode(symbolTable);
        symbolTable.write("BF " + afterWhileLabel + " ; while\n");

        for (SentenceNode sentenceNode : whileSentence) {
            sentenceNode.generateInterCode(symbolTable);
        }

        symbolTable.write("JUMP " + whileLabel + "\n" +
                afterWhileLabel + ": NOP\n");
    }
}
