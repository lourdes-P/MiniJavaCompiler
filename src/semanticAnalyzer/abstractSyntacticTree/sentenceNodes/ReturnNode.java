package semanticAnalyzer.abstractSyntacticTree.sentenceNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.statementExceptions.IncorrectTypeException;
import semanticAnalyzer.exceptions.part2.statementExceptions.InvalidReturnStatementException;
import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

import java.io.IOException;

public class ReturnNode extends SentenceNode {
    private ExpressionNode returnExpression;
    private Token returnToken;
    private Method containerMethod;
    private Block containerBlock;

    public ReturnNode(Token returnToken) {
        this.returnToken = returnToken;
        this.returnExpression = null;
        this.containerMethod = null;
    }

    public void setReturnExpression(ExpressionNode returnExpression) {
        this.returnExpression = returnExpression;
    }

    public void setContainerMethod(Method containerMethod) {
        this.containerMethod = containerMethod;
    }

    public void setContainerBlock(Block block) {
        this.containerBlock = block;
    }

    @Override
    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        if (returnExpression == null) {
            if (!containerMethod.getType().getName().equals("void"))
                throw new InvalidReturnStatementException(returnToken);
        } else {
            Type returnType = returnExpression.statementCheck(symbolTable);

            if (!containerMethod.getType().getName().equals("Object")) {
                if (!returnType.getName().equals("null")) {
                    if (!containerMethod.getType().getIsPrimitive() && !returnType.getIsPrimitive() && !containerMethod.getType().getType().equals(returnType.getType()) && !symbolTable.extendsClass(returnType.getToken(), containerMethod.getType().getToken())) {
                        throw new IncorrectTypeException(returnToken);
                    } else if (containerMethod.getType().getIsPrimitive() && !containerMethod.getType().getType().equals(returnType.getType())) {
                        throw new IncorrectTypeException(returnToken);
                    } else if ((!containerMethod.getType().getIsPrimitive() && returnType.getIsPrimitive()) || (containerMethod.getType().getIsPrimitive() && !returnType.getIsPrimitive())) {
                        throw new IncorrectTypeException(returnToken);
                    }
                } else {
                    if (containerMethod.getType().getName().equals("void")) {
                        throw new InvalidReturnStatementException(returnToken, containerMethod.getName());
                    }
                }
            }
        }
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        int numberOfParameters = containerMethod.getParameterCollection().size();

        returnExpression.generateInterCode(symbolTable);
        symbolTable.write("STORE " + (containerMethod.getIsStatic() ? numberOfParameters + 3 : numberOfParameters + 4) + "\n" +
                "FMEM "+ (- containerBlock.getOffset()) + " ; libero las celdas de memoria de las vars locales\n"+
                "STOREFP ; actualizar fp para que apunte al RA del llamador\n" +
                "RET 1 ; libero memoria de la celda de valor de retorno\n");
    }

}
