package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.VariableNotDeclaredException;
import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Variable;

import java.util.List;

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

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Variable var;

        if ((var = accessBlock.getAccessedVariable(idMetVar)) != null) {
            variable = var;
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
}
