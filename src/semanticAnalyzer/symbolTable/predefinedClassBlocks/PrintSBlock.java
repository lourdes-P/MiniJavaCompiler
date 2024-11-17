package semanticAnalyzer.symbolTable.predefinedClassBlocks;

import semanticAnalyzer.symbolTable.Block;
import semanticAnalyzer.symbolTable.Method;
import semanticAnalyzer.symbolTable.SymbolTable;

import java.io.IOException;

public class PrintSBlock extends Block {

    public PrintSBlock(Method containerMethod) {
        super(containerMethod);
        // imprime un String por salida estandar.
    }

    @Override
    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        symbolTable.write("""
                LOAD 3  ; Apila el parámetro
                PUSH 1
                ADD
                SPRINT
                """);
    }
}
