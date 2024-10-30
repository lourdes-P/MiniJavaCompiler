package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.IncompatibleTypeAssignmentException;
import semanticAnalyzer.exceptions.part2.statementExceptions.DuplicateLocalVariableNameException;
import semanticAnalyzer.exceptions.part2.statementExceptions.IncorrectTypeException;
import semanticAnalyzer.exceptions.part2.statementExceptions.NullDeclaredLocalVariableException;
import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.LocalVariable;

public class LocalVariableNode extends SentenceNode {
    private Token idMetVar;
    private LocalVariable variable;
    private ComposedExpressionNode rightSide;
    private Block containerBlock;

    public LocalVariableNode(Token idMetVar) {
        this.idMetVar = idMetVar;
    }

    public LocalVariableNode(Token idMetVar, Block containerBlock) {
        this.idMetVar = idMetVar;
        this.containerBlock = containerBlock;
    }

    public void setIdMetVar(Token idMetVar) {
        this.idMetVar = idMetVar;
    }

    public void setRightSide(ComposedExpressionNode rightSide) {
        this.rightSide = rightSide;
    }

    public void setVariable(LocalVariable variable) {
        this.variable = variable;
    }

    public void setContainerBlock(Block containerBlock) {
        this.containerBlock = containerBlock;
    }

    public LocalVariable getVariable() {
        return variable;
    }

    @Override
    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        if (variable.typeIsResolved()) {
            if (rightSide != null) {
                Type varType = rightSide.statementCheck(symbolTable);

                if (!variable.getType().getName().equals("Object")) {
                    if (!varType.getName().equals("null")) {
                        if (!variable.getType().getIsPrimitive() && !variable.getType().getType().equals(varType.getType()) && !symbolTable.extendsClass(varType.getToken(), variable.getType().getToken())) {
                            throw new IncorrectTypeException(idMetVar);
                        } else if (variable.getType().getIsPrimitive() && !variable.getType().getType().equals(varType.getType())) {
                            throw new IncorrectTypeException(idMetVar);
                        }
                    }
                }
            }
        } else {
            if (!containerBlock.declaredVariableUniqueToContainerMethod(idMetVar)) {
                throw new DuplicateLocalVariableNameException(idMetVar);
            } else {
                Type varType = rightSide.statementCheck(symbolTable);
                if (varType.getName().equals("null")) {
                    throw new NullDeclaredLocalVariableException(idMetVar);
                } else {
                    variable.setType(varType);
                }
            }
            // TODO testear que la variable sea visible por todas las sentencias subsiguientes
            // hasta que finalice el bloque actual.
        }
    }

    @Override
    public boolean isWhileOrSwitchStatement() {
        return false;
    }
}
