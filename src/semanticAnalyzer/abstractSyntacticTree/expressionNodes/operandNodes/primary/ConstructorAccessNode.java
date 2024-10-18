package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;


import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.symbolTable.types.Type;

import java.util.List;

public class ConstructorAccessNode extends PrimaryNode {
    private Token idClase;
    private List<ExpressionNode> actualArguments;
    private Type returnType;

    public ConstructorAccessNode(Token idClase, List<ExpressionNode> actualArguments, Type returnType) {
        this.idClase = idClase;
        this.actualArguments = actualArguments;
        this.returnType = returnType;
    }

    public String getName() {
        return idClase.getLexeme();
    }

    public Type getType() {
        return returnType;
    }
}
