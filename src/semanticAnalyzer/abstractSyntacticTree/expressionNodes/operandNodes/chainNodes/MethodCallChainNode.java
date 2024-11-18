package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.chainNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.*;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Parameter;
import utils.LabelFactory;

import java.io.IOException;
import java.util.List;

public class MethodCallChainNode extends ChainNode {
    private List<ExpressionNode> actualArguments;
    private String className;


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
        className = primaryNodeType.getName();
        if (!primaryNodeType.getIsPrimitive() && symbolTable.containsClass(className)) {
            if (!symbolTable.getClass(className).hasMethod(methodName))
                throw new MethodNotDeclaredException(getIdMetVar());
        } else {
            throw new InvalidChainedMethodException(getIdMetVar(), className);
        }

        Method method = symbolTable.getClass(className).getMethod(methodName);
        List<Parameter> formalArgumentList = method.getOrderedParameterList();

        if (actualArguments.size() == formalArgumentList.size()) {
            for (int i = 0; i < actualArguments.size() ; i++) {
                Type actualArgumentType = actualArguments.get(i).statementCheck(symbolTable);

                if(formalArgumentList.get(i).isTypePrimitive() && actualArgumentType.getName().equals("null")) {
                    throw new InvalidActualArgumentException(getIdMetVar(), actualArgumentType.getToken());
                } else if (!actualArgumentType.getIsPrimitive() && !formalArgumentList.get(i).isTypePrimitive() && (!actualArgumentType.getType().equals((formalArgumentList.get(i).getType().getType())) && !symbolTable.extendsClass(new Token("idClase", actualArgumentType.getType(), actualArgumentType.getToken().getLineNumber()), formalArgumentList.get(i).getType().getToken()))) {
                    throw new InvalidActualArgumentException(getIdMetVar(), actualArgumentType.getToken());
                } else if ((actualArgumentType.getIsPrimitive() && !formalArgumentList.get(i).isTypePrimitive()) || (!actualArgumentType.getIsPrimitive() && formalArgumentList.get(i).isTypePrimitive())) {
                    throw new InvalidActualArgumentException(getIdMetVar(), actualArgumentType.getToken());
                } else if (actualArgumentType.getIsPrimitive() && !actualArgumentType.getType().equals(formalArgumentList.get(i).getType().getType())) {
                    throw new InvalidActualArgumentException(getIdMetVar(), actualArgumentType.getToken());
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

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        Method calledMethod = symbolTable.getClass(className).getMethod(this.getIdMetVar().getLexeme());

        if (!calledMethod.getType().getName().equals("void")) {
            symbolTable.write("RMEM 1 ; reservo memoria en la pila para el valor de retorno\n");
            if (!calledMethod.getIsStatic()) {
                symbolTable.write("SWAP ; para llevarme el this\n");
            }
        }

        for (ExpressionNode expressionNode : actualArguments) {
            expressionNode.generateInterCode(symbolTable);
            if (!calledMethod.getIsStatic()) {
                symbolTable.write("SWAP ; para llevarme el this\n");
            }
        }
        if (!calledMethod.getIsStatic()) {
            symbolTable.write("DUP\n" +
                    "LOADREF 0 ; cargo una referencia a la VT\n" +
                    "LOADREF " + calledMethod.getOffset() + " ; cargo la direccion del metodo en la VT\n" +
                    "CALL\n");
        } else {
            symbolTable.write("PUSH " + LabelFactory.createLabel("met", calledMethod.getName(), calledMethod.getContainerClass().getName()) + "\n"+
                    "CALL\n");
        }

        if (getFurtherChainNode() != null) {
            getFurtherChainNode().setIsLeftSideOfAssignment(isLeftSideOfAssignment());
            getFurtherChainNode().setIsLeftSideOfAssignment(this.isLeftSideOfAssignment());
            getFurtherChainNode().setIsCallStatement(isCallStatement());
            getFurtherChainNode().generateInterCode(symbolTable);
        }

        if (isCallStatement() && getFurtherChainNode()==null){
            // TODO  chequear si llamada a constructor es callstatement
            if (!calledMethod.getType().getName().equals("void"))
                symbolTable.write("POP ; la llamada devolvio algo distinto de void -> se descarta\n");
        }
    }


}
