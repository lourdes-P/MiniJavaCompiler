package semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary;


import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part1.ClassNotDeclaredException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.DifferentNumberOfArgumentsException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidActualArgumentException;
import semanticAnalyzer.symbolTable.Constructor;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.ReferenceType;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Parameter;

import java.io.IOException;
import java.util.List;

public class ConstructorAccessNode extends PrimaryNode {
    private Token idClase;
    private List<ExpressionNode> actualArguments;
    private Type returnType;

    public ConstructorAccessNode(Token idClase) {
        this.idClase = idClase;
        returnType = new ReferenceType(idClase);
    }

    public ConstructorAccessNode(Token idClase, List<ExpressionNode> actualArguments, Type returnType) {
        this.idClase = idClase;
        this.actualArguments = actualArguments;
        this.returnType = returnType;
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        String className = idClase.getLexeme();
        if (!symbolTable.containsClass(className))
            throw new ClassNotDeclaredException(idClase);

        Constructor constructor = symbolTable.getClassConstructor(className);
        List<Parameter> formalArgumentList = constructor.getOrderedParameterList();

        if (actualArguments.size() == formalArgumentList.size()) {
            for (int i = 0; i < actualArguments.size() ; i++) {
                Type actualArgumentType = actualArguments.get(i).statementCheck(symbolTable);

                if(formalArgumentList.get(i).isTypePrimitive() && actualArgumentType.getName().equals("null")) {
                    throw new InvalidActualArgumentException(idClase, actualArgumentType.getToken());
                } else if (!actualArgumentType.getIsPrimitive() && !formalArgumentList.get(i).isTypePrimitive() && (!actualArgumentType.getType().equals((formalArgumentList.get(i).getType().getType())) && !symbolTable.extendsClass(new Token("idClase", actualArgumentType.getType(), actualArgumentType.getToken().getLineNumber()), formalArgumentList.get(i).getType().getToken()))) {
                        throw new InvalidActualArgumentException(idClase, actualArgumentType.getToken());
                } else if ((actualArgumentType.getIsPrimitive() && !formalArgumentList.get(i).isTypePrimitive()) || (!actualArgumentType.getIsPrimitive() && formalArgumentList.get(i).isTypePrimitive())) {
                    throw new InvalidActualArgumentException(idClase, actualArgumentType.getToken());
                } else if (actualArgumentType.getIsPrimitive() && !actualArgumentType.getType().equals(formalArgumentList.get(i).getType().getType())) {
                    throw new InvalidActualArgumentException(idClase, actualArgumentType.getToken());
                }
            }
        } else {
            throw new DifferentNumberOfArgumentsException(idClase);
        }

        return returnType;
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
        return idClase;
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable, boolean chainIsNull) throws IOException {
        // deja en el tope de la pila una referencia al CIR del objeto creado

    }
}
