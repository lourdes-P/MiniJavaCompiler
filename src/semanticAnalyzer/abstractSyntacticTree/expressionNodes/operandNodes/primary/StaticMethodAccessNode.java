package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;

import java.util.List;

public class StaticMethodAccessNode extends PrimaryNode {
    private Token idClase, idMetVar;
    private List<ExpressionNode> actualArguments;

    public StaticMethodAccessNode(Token idClase) {
        this.idClase = idClase;
    }

    public StaticMethodAccessNode(Token idClase, Token idMetVar, List<ExpressionNode> actualArguments) {
        this.idClase = idClase;
        this.idMetVar = idMetVar;
        this.actualArguments = actualArguments;
    }

    public void setIdMetVar(Token idMetVar) {
        this.idMetVar = idMetVar;
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    public String getName() {
        return idMetVar.getLexeme();
    }

    public String getClassName() {
        return idClase.getLexeme();
    }
}
