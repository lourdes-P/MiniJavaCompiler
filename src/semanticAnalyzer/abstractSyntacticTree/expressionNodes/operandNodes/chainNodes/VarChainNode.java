package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.chainNodes;

import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidChainedVariableAccessException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Attribute;
import utils.LabelFactory;

import java.io.IOException;

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

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        if (!attribute.isStatic()) {
            if (!this.isLeftSideOfAssignment() || !(getFurtherChainNode() == null)) {
                symbolTable.write("LOADREF " + attribute.getOffset() + "\n");
            } else {
                symbolTable.write("SWAP\n" +
                        "STOREREF " + attribute.getOffset() + "\n");
            }
        } else {
            symbolTable.write("POP ; descarto\n");
            symbolTable.write("PUSH " + LabelFactory.createLabel("attr", attribute.getName(), attribute.getContainerClass().getName()) + "\n");

            if (!this.isLeftSideOfAssignment() || !(getFurtherChainNode() == null)) {
                symbolTable.write("LOADREF 0 ; cargo valor atributo estatico\n");
            } else {
                symbolTable.write("""
                            SWAP
                            STOREREF 0 ; guardo en atributo estatico
                            """);
            }
        }

        if (getFurtherChainNode() != null) {
            getFurtherChainNode().setIsLeftSideOfAssignment(isLeftSideOfAssignment());
            getFurtherChainNode().setIsLeftSideOfAssignment(this.isLeftSideOfAssignment());
            getFurtherChainNode().setIsCallStatement(isCallStatement());
            getFurtherChainNode().generateInterCode(symbolTable);
        }
    }

}
