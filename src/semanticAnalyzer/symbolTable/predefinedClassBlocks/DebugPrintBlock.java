package semanticAnalyzer.symbolTable.predefinedClassBlocks;

import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;

import java.io.IOException;

public class DebugPrintBlock extends Block {

    public DebugPrintBlock(Method containerMethod) {
        super(containerMethod);
        // imprime un entero por la salida estandar y finaliza la linea.
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        symbolTable.write("""
                LOAD 3  ; Apila el parámetro
                IPRINT
                PRNLN
                """);
    }
}
