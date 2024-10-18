package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.symbolTable.types.Type;

import java.util.ArrayList;
import java.util.List;

public class ChainNode {
    private ChainNode furtherChainNode;
    private Token idMetVar;
    private List<ExpressionNode> actualArguments;
    private Type finalType;



}
