package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.VariableNotDeclaredException;
import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Attribute;
import semanticAnalyzer.symbolTable.variables.Variable;

import java.io.IOException;

public class VarAccessNode extends PrimaryNode {
    private Token idMetVar;
    private Block accessBlock;
    private Variable variable;

    public VarAccessNode(Token idMetVar) {
        this.idMetVar = idMetVar;
    }

    public void setAccessBlock(Block accessBlock) {
        this.accessBlock = accessBlock;
    }

    public String getName() {
        return idMetVar.getLexeme();
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Variable var;

        if (accessBlock != null && (var = accessBlock.getAccessedVariable(idMetVar)) != null) {
            variable = var;
        } else if (accessBlock == null && symbolTable.getClass(((Attribute) variable).getContainerClass().getName()).hasAttribute(idMetVar.getLexeme())){
            return ((Attribute) variable).getContainerClass().getAttribute(idMetVar.getLexeme()).getType();
        } else {
            throw new VariableNotDeclaredException(idMetVar);
        }

        return variable.getType();
    }

    @Override
    public boolean canBeAssignedAValue() {
        return true;
    }

    @Override
    public boolean canBeCalled() {
        return false;
    }

    @Override
    public Token getToken() {
        return idMetVar;
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable, boolean chainIsNull) throws IOException {
        if (variable instanceof Attribute) {
            symbolTable.write("LOAD 3 ; cargo this\n");
            if (!this.isLeftSideOfAssignment() || !chainIsNull) {
                symbolTable.write("LOADREF " + variable.getOffset() + "\n");
            } else {
                symbolTable.write("SWAP\n" +
                        "STOREREF " + variable.getOffset() + "\n");
            }
        } else {
            if (!this.isLeftSideOfAssignment() || !chainIsNull) {
                symbolTable.write("LOAD " + variable.getOffset() + "\n");
            } else {
                symbolTable.write("STORE " + variable.getOffset() + "\n");
            }
        }

        // a la cadena se le dice que se genere en el AccessNode.
    }
}
