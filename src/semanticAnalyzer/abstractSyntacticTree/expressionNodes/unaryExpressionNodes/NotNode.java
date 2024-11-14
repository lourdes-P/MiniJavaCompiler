package semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidOperandTypeForUnaryOperatorException;
import semanticAnalyzer.symbolTable.SymbolTable;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.Type;

public class NotNode extends UnaryExpressionNode{

    public NotNode(OperandNode operand, Token operator) {
        super(operand, operator);
    }

    public NotNode(Token operator) {
        super(operator);
    }

    public Type statementCheck(SymbolTable symbolTable) throws SemanticException {
        Type operandType = getOperandNode().statementCheck(symbolTable);
        if(operandType.getType().equals("boolean")){
            if(operandType.getName().equals("true")){
                return new PrimitiveType(new Token("pr_false", "false", operandType.getToken().getLineNumber()));
            }else{
                return new PrimitiveType(new Token("pr_true", "true", operandType.getToken().getLineNumber()));
            }
        }else{
            throw new InvalidOperandTypeForUnaryOperatorException(this.getToken());
        }
    }


}
