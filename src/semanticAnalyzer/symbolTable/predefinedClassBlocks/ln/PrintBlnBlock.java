package semanticAnalyzer.symbolTable.predefinedClassBlocks.ln;

import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;

import java.io.IOException;

public class PrintBlnBlock extends Block {

    public PrintBlnBlock(Method containerMethod) {
        super(containerMethod);
        //  imprime un boolean por salida estandar y finaliza la linea actual.
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        symbolTable.write("""
                LOAD 3 ; apilo el parametro
                BPRINT
                PRNLN
                STOREFP ; almaceno el tope de la pila en el registro fp
                RET 1
                """);
    }
}
