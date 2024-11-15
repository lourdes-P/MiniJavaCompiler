package semanticAnalyzer.symbolTable.predefinedClassBlocks;

import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;

import java.io.IOException;

public class PrintIBlock extends Block {

    public PrintIBlock(Method containerMethod) {
        super(containerMethod);
        // imprime un entero por la salida estandar y finaliza la linea.
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        symbolTable.write("""
                LOAD 3 ; apilo el parametro
                IPRINT
                STOREFP ; almaceno el tope de la pila en el registro fp
                RET 1
                """);
    }


}
