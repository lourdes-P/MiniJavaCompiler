package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.DuplicateParameterException;
import semanticAnalyzer.exceptions.InvalidConstructorException;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.type.ReferenceType;
import semanticAnalyzer.symbolTable.type.Type;

import java.util.HashMap;
import java.util.List;

public class Constructor extends Method {

    public Constructor(Token token, Class containerClass) throws InvalidConstructorException {
        super(true, token, containerClass, new ReferenceType(containerClass.getToken()));
        if (!token.getLexeme().equals(containerClass.getName()))
            throw new InvalidConstructorException(containerClass, this);
    }


}
