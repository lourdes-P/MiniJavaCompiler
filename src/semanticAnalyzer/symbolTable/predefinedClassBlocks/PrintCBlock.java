package semanticAnalyzer.symbolTable.predefinedClassBlocks;

import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;

import java.io.IOException;

public class PrintCBlock extends Block {

    public PrintCBlock(Method containerMethod) {
        super(containerMethod);
        // imprime un char por la salida estandar.
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        symbolTable.write("""
                LOAD 3  ; Apila el parámetro
                CPRINT                
                """);
    }
}