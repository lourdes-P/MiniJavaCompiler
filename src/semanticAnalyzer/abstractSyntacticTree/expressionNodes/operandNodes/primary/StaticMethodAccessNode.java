package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.DifferentNumberOfArgumentsException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidActualArgumentException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidStaticCallException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.MethodNotDeclaredException;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Parameter;

import java.util.List;

public class StaticMethodAccessNode extends PrimaryNode {
    private Token idClase, idMetVar;
    private List<ExpressionNode> actualArguments;
    private Method containerMethod;
    private Type type;

    public StaticMethodAccessNode(Token idClase) {
        this.idClase = idClase;
    }

    public StaticMethodAccessNode(Token idClase, Token idMetVar, List<ExpressionNode> actualArguments) {
        this.idClase = idClase;
        this.idMetVar = idMetVar;
        this.actualArguments = actualArguments;
    }

    public void setIdMetVar(Token idMetVar) {
        this.idMetVar = idMetVar;
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    public String getName() {
        return idMetVar.getLexeme();
    }

    public String getClassName() {
        return idClase.getLexeme();
    }


    public void setContainerMethod(Method containerMethod) {
        this.containerMethod = containerMethod;
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        String methodName = idMetVar.getLexeme();
        if (!symbolTable.getClass(idClase.getLexeme()).hasMethod(methodName))
            throw new MethodNotDeclaredException(idMetVar, idClase);

        Method method = symbolTable.getClass(idClase.getLexeme()).getMethod(methodName);
        type = method.getType();
        List<Parameter> formalArgumentList = method.getOrderedParameterList();

        if (!method.getIsStatic())
            throw new InvalidStaticCallException(idMetVar);

        if (actualArguments.size() == formalArgumentList.size()) {
            for (int i = 0; i < actualArguments.size() ; i++) {
                Type actualArgumentType = actualArguments.get(i).statementCheck(symbolTable);

                if(formalArgumentList.get(i).isTypePrimitive() && actualArgumentType.getName().equals("null")) {
                    throw new InvalidActualArgumentException(idMetVar, actualArgumentType.getToken());
                } else if (!actualArgumentType.getIsPrimitive() && !formalArgumentList.get(i).isTypePrimitive() && (!actualArgumentType.getType().equals((formalArgumentList.get(i).getType().getType())) && !symbolTable.extendsClass(new Token("idClase", actualArgumentType.getType(), actualArgumentType.getToken().getLineNumber()), formalArgumentList.get(i).getType().getToken()))) {
                    throw new InvalidActualArgumentException(idMetVar, actualArgumentType.getToken());
                } else if ((actualArgumentType.getIsPrimitive() && !formalArgumentList.get(i).isTypePrimitive()) || (!actualArgumentType.getIsPrimitive() && formalArgumentList.get(i).isTypePrimitive())) {
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
}
