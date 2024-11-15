package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidDynamicThisUseException;
import semanticAnalyzer.symbolTable.Class;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.ReferenceType;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public class ThisAccessNode extends PrimaryNode {
    private Token thisToken;
    private Class thisClass;
    private Method containerMethod;

    public ThisAccessNode(Token thisToken, Class thisClass, Method containerMethod) {
        this.thisToken = thisToken;
        this.thisClass = thisClass;
        this.containerMethod = containerMethod;
    }

    public ThisAccessNode(Token thisToken) {
        this.thisToken = thisToken;
        this.thisClass = null;
        this.containerMethod = null;
    }

    public void setThisClass(Class thisClass) {
        this.thisClass = thisClass;
    }

    public void setContainerMethod(Method containerMethod) {
        this.containerMethod = containerMethod;
    }

    @Override
    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        if(containerMethod!= null && containerMethod.getIsStatic())
            throw new InvalidDynamicThisUseException(thisToken);

        return new ReferenceType(new Token("idClase", thisClass.getName(), thisToken.getLineNumber()));
    }

    public boolean canBeAssignedAValue() {
        return false;
    }

    @Override
    public boolean canBeCalled() {
        return false;
    }

    @Override
    public Token getToken() {
        return thisToken;
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable, boolean chainIsNull) throws IOException {
        // TODO thisNode
    }
}
