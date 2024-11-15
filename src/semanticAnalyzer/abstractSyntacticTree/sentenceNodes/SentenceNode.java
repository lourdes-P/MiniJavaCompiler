package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;

import java.io.IOException;

public abstract class SentenceNode {

    public abstract void statementCheck(SymbolTable symbolTable) throws SemanticException;

    public abstract void generateInterCode(SymbolTable symbolTable) throws IOException;
}
