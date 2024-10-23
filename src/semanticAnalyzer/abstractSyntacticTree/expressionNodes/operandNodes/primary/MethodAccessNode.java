package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.AccessNode;
import semanticAnalyzer.symbolTable.types.Type;

import java.util.List;

public class MethodAccessNode extends PrimaryNode {
    private Token idMetVar;
    private List<ExpressionNode> actualArguments;
    private Type type;


    public MethodAccessNode(Token idMetVar) {
        this.idMetVar = idMetVar;
    }

    public MethodAccessNode(Token idMetVar, List<ExpressionNode> actualArguments, Type type) {
        this.idMetVar = idMetVar;
        this.actualArguments = actualArguments;
        this.type = type;
    }

    public String getName() {
        return idMetVar.getLexeme();
    }

    public Type getType() {
        return type;
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
