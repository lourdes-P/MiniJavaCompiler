package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.chainNodes;

import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidChainedVariableAccessException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Attribute;

public class VarChainNode extends ChainNode {
    private Attribute attribute;


    @Override
    public Type statementCheck(Type primaryNodeType, SymbolTable symbolTable) throws SemanticException {
        if (!primaryNodeType.getIsPrimitive() && symbolTable.containsClass(primaryNodeType.getName())) {
            attribute = symbolTable.getClass(primaryNodeType.getName()).getAttribute(getIdMetVar().getLexeme());
            if (attribute == null) {
                throw new InvalidChainedVariableAccessException(getIdMetVar(), primaryNodeType.getName());
            }
        } else
            throw new InvalidChainedVariableAccessException(getIdMetVar(), primaryNodeType.getName());

        if (getFurtherChainNode() == null) {
            setFinalType(attribute.getType());
            return getFinalType();
        } else {
            return getFurtherChainNode().statementCheck(attribute.getType(), symbolTable);
        }
    }

    @Override
    public boolean canBeAssignedAValue() {
        if (getFurtherChainNode() == null)
            return true;
        else
            return getFurtherChainNode().canBeAssignedAValue();
    }

}
