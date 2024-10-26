package semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

public abstract class SwitchSentenceNode {
    private Token switchSentenceToken;

    public SwitchSentenceNode(Token token) {
        switchSentenceToken = token;
    }

    public abstract void statementCheck(Type conditionType, SymbolTable symbolTable) throws SemanticException;

    public Token getSwitchSentenceToken() {
        return switchSentenceToken;
    }
}
