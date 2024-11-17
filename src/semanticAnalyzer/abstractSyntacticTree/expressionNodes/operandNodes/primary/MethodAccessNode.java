package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.DifferentNumberOfArgumentsException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidActualArgumentException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidDynamicCallException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.MethodNotDeclaredException;
import semanticAnalyzer.symbolTable.Class;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Parameter;
import utils.LabelFactory;

import java.io.IOException;
import java.util.List;

public class MethodAccessNode extends PrimaryNode {
    private Token idMetVar;
    private List<ExpressionNode> actualArguments;
    private Type type;
    private Class containerClass;
    private Method containerMethod, calledMethod;


    public MethodAccessNode(Token idMetVar) {
        this.idMetVar = idMetVar;
    }

    public MethodAccessNode(Token idMetVar, List<ExpressionNode> actualArguments, Type type) {
        this.idMetVar = idMetVar;
        this.actualArguments = actualArguments;
        this.type = type;
    }

    public String getName() {
        return idMetVar.getLexeme();
    }

    public Type getType() {
        return type;
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setClass(Class class_) {
        containerClass = class_;
    }

    public void setContainerMethod(Method containerMethod) {
        this.containerMethod = containerMethod;
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        String className = containerClass.getName();
        String methodName = idMetVar.getLexeme();
        if (!symbolTable.getClass(className).hasMethod(methodName))
            throw new MethodNotDeclaredException(idMetVar);

        Method method = symbolTable.getClass(className).getMethod(methodName);
        type = method.getType();
        List<Parameter> formalArgumentList = method.getOrderedParameterList();

        if (containerMethod.getIsStatic() && !method.getIsStatic())
            throw new InvalidDynamicCallException(idMetVar);

        if (actualArguments.size() == formalArgumentList.size()) {
            for (int i = 0; i < actualArguments.size() ; i++) {
                Type actualArgumentType = actualArguments.get(i).statementCheck(symbolTable);

                if(formalArgumentList.get(i).isTypePrimitive() && actualArgumentType.getName().equals("null")) {
                    throw new InvalidActualArgumentException(idMetVar, actualArgumentType.getToken());
                } else if (!actualArgumentType.getIsPrimitive() && !formalArgumentList.get(i).isTypePrimitive() && (!actualArgumentType.getType().equals((formalArgumentList.get(i).getType().getType())) && !symbolTable.extendsClass(new Token("idClase", actualArgumentType.getType(), actualArgumentType.getToken().getLineNumber()), formalArgumentList.get(i).getType().getToken()))) {
                    throw new InvalidActualArgumentException(idMetVar, actualArgumentType.getToken());
                } else if ((actualArgumentType.getIsPrimitive() && !formalArgumentList.get(i).isTypePrimitive()) || (!actualArgumentType.getIsPrimitive() && formalArgumentList.get(i).isTypePrimitive())) {
                    throw new InvalidActualArgumentException(idMetVar, actualArgumentType.getToken());
                } else if (actualArgumentType.getIsPrimitive() && !actualArgumentType.getType().equals(formalArgumentList.get(i).getType().getType())) {
                    throw new InvalidActualArgumentException(idMetVar, actualArgumentType.getToken());
                }
            }
        } else {
            throw new DifferentNumberOfArgumentsException(idMetVar);
        }

        return type;
    }

    public boolean canBeAssignedAValue() {
        return false;
    }

    @Override
    public boolean canBeCalled() {
        return true;
    }

    @Override
    public Token getToken() {
        return idMetVar;
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable, boolean chainIsNull) throws IOException {
        calledMethod = symbolTable.getClass(containerClass.getName()).getMethod(idMetVar.getLexeme());

        if (!calledMethod.getIsStatic()) {
            symbolTable.write("LOAD 3 ; cargo this\n");
        }
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
            symbolTable.write("DUP ; methodAccessNode\n" +
                    "LOADREF 0 ; cargo una referencia a la VT\n" +
                    "LOADREF " + calledMethod.getOffset() + " ; cargo la direccion del metodo en la VT\n" +
                    "CALL\n");
        } else {
            symbolTable.write("PUSH " + LabelFactory.createLabel("met", calledMethod.getName(), calledMethod.getContainerClass().getName()) + "\n"+
                    "CALL\n");
        }

        if (isCallStatement()){
            if (!calledMethod.getType().getName().equals("void"))
                symbolTable.write("POP ; la llamada devolvio algo distinto de void -> se descarta\n");
        }
    }
}
