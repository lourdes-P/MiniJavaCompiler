package semanticAnalyzer.symbolTable.predefinedClassBlocks;

import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;

import java.io.IOException;

public class ReadBlock extends Block {

    public ReadBlock(Method containerMethod) {
        super(containerMethod);
        // lee el proximo byte del stream de entrada estandar.
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        symbolTable.write("""
                READ ; lee un valor entero
                PUSH 48 ; subtraction por ascii
                SUB
                STORE 3 ; almaceno el tope en una locacion reservada para retornar
                """);
    }
}
