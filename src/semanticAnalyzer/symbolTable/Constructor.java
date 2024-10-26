package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.part1.InvalidConstructorException;
import semanticAnalyzer.symbolTable.types.ReferenceType;

public class Constructor extends Method {

    public Constructor(Token token, Class containerClass) throws InvalidConstructorException {
        super(true, token, containerClass, new ReferenceType(containerClass.getToken()));
        if (!token.getLexeme().equals(containerClass.getName()))
            throw new InvalidConstructorException(containerClass, this);
    }


}
