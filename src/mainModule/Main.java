package mainModule;

import ioManager.SourceManager;
import ioManager.SourceManagerImpl;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.exceptions.LexicalException;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.reservedWordManager.ReservedWordMap;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.SymbolTable;
import syntacticAnalyzer.SyntacticAnalyzer;
import syntacticAnalyzer.exceptions.AbstractSyntacticException;
import utils.FirstsManager;
import utils.Formatter;
import utils.MapManager;
import utils.NextsManager;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String sourceFile = "";
        if (args.length == 0) {
            sourceFile = "dummySourceFile.txt";
            System.out.println("No hay archivo fuente como argumento.");
        } else {
            sourceFile = args[0];
        }
        ReservedWordMap reservedWordMap = new ReservedWordMap();
        SourceManager sourceManager = new SourceManagerImpl();
        try {
            sourceManager.open(sourceFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error al intentar abrir el archivo fuente.");
        }

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager, reservedWordMap);
        SymbolTable symbolTable = null;
        try {
            symbolTable = new SymbolTable();
        } catch (SemanticException semanticException) {
            System.out.println(semanticException.getMessage());
        }
        SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer, symbolTable);

        try {
            syntacticAnalyzer.start();
            symbolTable.checkDeclarations();
            symbolTable.consolidate();
            symbolTable.statementCheck();

            checkOffsets(symbolTable);
        } catch (LexicalException lexicalException) {
            System.out.println(lexicalException.getMessage());
            lexicalAnalyzer.registerLexicalError();
        } catch (AbstractSyntacticException syntacticException) {
            System.out.println(syntacticException.getMessage());
            syntacticAnalyzer.registerSyntacticError();
        } catch (SemanticException semanticException) {
            System.out.println(semanticException.getMessage());
            syntacticAnalyzer.registerSyntacticError();
        }

        if (lexicalAnalyzer.getSinErrores() && syntacticAnalyzer.getSinErrores()) {
            System.out.println("[SinErrores]");
        }
    }

    private static void checkOffsets(SymbolTable symbolTable) {
        symbolTable.checkOffsets();
    }

/*
    public static void main(String[] args) {

        String sourceFile = "";
        if (args.length == 0) {
            sourceFile = "dummySourceFile.txt";
            System.out.println("No hay archivo fuente como argumento.");
        } else {
            sourceFile = args[0];
        }
        ReservedWordMap reservedWordMap = new ReservedWordMap();
        //reservedWordMap.showMap();
        SourceManager sourceManager = new SourceManagerImpl();
        try {
            sourceManager.open(sourceFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error al intentar abrir el archivo fuente.");
        }

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager, reservedWordMap);
        SymbolTable symbolTable = null;
        try {
            symbolTable = new SymbolTable();
        } catch (SemanticException semanticException) {
            System.out.println(semanticException.getMessage());
        }
        SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer,symbolTable);

        try {
            syntacticAnalyzer.start();
        } catch (LexicalException lexicalException) {
            System.out.println(lexicalException.getMessage());
            lexicalAnalyzer.registerLexicalError();
        } catch (AbstractSyntacticException | SemanticException syntacticException) {
            System.out.println(syntacticException.getMessage());
            syntacticAnalyzer.registerSyntacticError();
        }

        if (lexicalAnalyzer.getSinErrores() && syntacticAnalyzer.getSinErrores()) {
            System.out.println("[SinErrores]");
        }

    }

 */


}