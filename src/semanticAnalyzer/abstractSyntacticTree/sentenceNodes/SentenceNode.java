package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;

public abstract class SentenceNode {

    public abstract void statementCheck(SymbolTable symbolTable) throws SemanticException;

    public abstract boolean isWhileOrSwitchStatement();
}
