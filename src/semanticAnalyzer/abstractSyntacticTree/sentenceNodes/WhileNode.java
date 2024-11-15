package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.InvalidWhileConditionTypeException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;
import java.util.List;

public class WhileNode extends SentenceNode {
    private ExpressionNode condition;
    private List<SentenceNode> whileSentence;
    private Token whileToken;

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
        // TODO whileNode
    }
}
