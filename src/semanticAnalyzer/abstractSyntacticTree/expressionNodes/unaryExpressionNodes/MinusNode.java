package semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidOperandTypeForUnaryOperatorException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.Type;

public class MinusNode extends UnaryExpressionNode {

    public MinusNode(OperandNode operand, Token operator) {
        super(operand, operator);
    }

    public MinusNode(Token operator) {
        super(operator);
    }

    @Override
    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type operandType = getOperandNode().statementCheck(symbolTable);
        if(operandType.getType().equals("int")){
            return operandType;
        }else{
            throw new InvalidOperandTypeForUnaryOperatorException(this.getOperatorToken());
        }
    }

}
