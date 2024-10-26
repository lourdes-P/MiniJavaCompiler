package semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal.PrimitiveLiteralNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.InvalidSwitchConditionTypeException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.util.List;

public class SwitchCaseSentenceNode extends SwitchSentenceNode {
    private PrimitiveLiteralNode primitiveLiteralNode;
    private List<SentenceNode> optionalSentence;


    public SwitchCaseSentenceNode(Token switchCaseToken) {
        super(switchCaseToken);
        primitiveLiteralNode = null;
        optionalSentence = null;
    }

    public void setPrimitiveLiteralNode(PrimitiveLiteralNode primitiveLiteralNode) {
        this.primitiveLiteralNode = primitiveLiteralNode;
    }

    public void setOptionalSentence(List<SentenceNode> optionalSentence) {
        this.optionalSentence = optionalSentence;
    }

    public void statementCheck(Type conditionType, SymbolTable symbolTable) throws SemanticException {
        Type primitiveLiteralType = primitiveLiteralNode.statementCheck(symbolTable);
        if (!primitiveLiteralType.getType().equals(conditionType.getType()))
            throw new InvalidSwitchConditionTypeException(getSwitchSentenceToken(), conditionType.getType());

        for (SentenceNode sentenceNode : optionalSentence) {
            sentenceNode.statementCheck(symbolTable);
        }
    }
}
