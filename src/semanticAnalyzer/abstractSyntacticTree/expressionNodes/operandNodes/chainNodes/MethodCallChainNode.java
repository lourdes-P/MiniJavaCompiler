package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.chainNodes;

import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.*;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Parameter;

import java.util.List;

public class MethodCallChainNode extends ChainNode {
    private List<ExpressionNode> actualArguments;


    public MethodCallChainNode() {
        super();
    }

    public List<ExpressionNode> getActualArguments() {
        return actualArguments;
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    @Override
    public Type statementCheck(Type primaryNodeType, SymbolTable symbolTable) throws SemanticException {
        String methodName = getIdMetVar().getLexeme();
        String className = primaryNodeType.getName();
        if (!primaryNodeType.getIsPrimitive() && symbolTable.containsClass(className)) {
            if (!symbolTable.getClass(className).hasMethod(methodName))
                throw new MethodNotDeclaredException(getIdMetVar());
        } else {
            throw new InvalidChainedMethodException(getIdMetVar(), className);
        }

        Method method = symbolTable.getClass(className).getMethod(methodName);
        List<Parameter> formalArgumentList = method.getOrderedParameterList();

        if (this.getContainerMethod().getIsStatic() && !method.getIsStatic())
            throw new InvalidDynamicCallException(getIdMetVar());

        if (actualArguments.size() == formalArgumentList.size()) {
            for (int i = 0; i < actualArguments.size() ; i++) {
                Type actualArgumentType = actualArguments.get(i).statementCheck(symbolTable);
                if (!(actualArgumentType.getName().equals("null") && !formalArgumentList.get(i).isTypePrimitive()) ||
                        !(!actualArgumentType.getIsPrimitive() && !formalArgumentList.get(i).isTypePrimitive() && symbolTable.extendsClass(actualArgumentType.getToken(), formalArgumentList.get(i).getToken())) ||
                        (!actualArgumentType.getType().equals((formalArgumentList.get(i).getType().getType())))) {
                    throw new InvalidActualArgumentException(actualArgumentType.getToken());
                }
            }
        } else {
            throw new DifferentNumberOfArgumentsException(getIdMetVar());
        }

        if (getFurtherChainNode() == null) {
            setFinalType(method.getType());
            return method.getType();
        } else {
            return getFurtherChainNode().statementCheck(method.getType(), symbolTable);
        }
    }

    @Override
    public boolean canBeAssignedAValue() {
        if (getFurtherChainNode() == null)
            return false;
        else
            return getFurtherChainNode().canBeAssignedAValue();
    }


}
