package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.symbolTable.Class;

public class ThisAccessNode extends PrimaryNode {
    private Token thisToken;
    private Class thisClass;

    public ThisAccessNode(Token thisToken, Class thisClass) {
        this.thisToken = thisToken;
        this.thisClass = thisClass;
    }

}
