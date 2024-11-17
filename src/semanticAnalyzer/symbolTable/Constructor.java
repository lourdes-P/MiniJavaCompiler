package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.part1.InvalidConstructorException;
import semanticAnalyzer.symbolTable.types.ReferenceType;
import utils.LabelFactory;

import java.io.IOException;

public class Constructor extends Method {

    public Constructor(Token token, Class containerClass) throws InvalidConstructorException {
        super(false, token, containerClass, new ReferenceType(containerClass.getToken()));
        if (!token.getLexeme().equals(containerClass.getName()))
            throw new InvalidConstructorException(containerClass, this);
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        // el llamador es quien guarda memoria para el retorno (RMEM)
        // el desplazamiento del ret_val será m+3. (m celdas de memoria correspondiente a los m parametros).
        // el this lo agrega la unidad llamadora
        symbolTable.write(LabelFactory.createLabel("ctor", this.getName(), this.getContainerClass().getName()) + ": LOADFP ; apila el valor del registro fp\n" +
                "LOADSP ; apila el valor del registro sp\n" +
                "STOREFP ; almacena el tope de la pila en el registro fp\n");

        this.getMainBlock().generateInterCode(symbolTable);

        symbolTable.write("STOREFP ; almacena el tope de la pila en el registro\n" +
                "RET " + (1 + getParameterCollection().size()) + "\n");
    }

}
