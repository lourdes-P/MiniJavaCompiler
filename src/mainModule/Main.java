package mainModule;

import ioManager.SourceManager;
import ioManager.SourceManagerImpl;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.LexicalException;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.reservedWordManager.ReservedWordMap;
import utils.Formatter;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        String sourceFile = "";
        if (args.length == 0) {
            sourceFile = "./resources/dummySourceFile.txt";
            System.out.println("No hay archivo fuente como argumento.");
        } else {
            sourceFile = args[0];
        }
        ReservedWordMap reservedWordMap = new ReservedWordMap();
        //TODO descomentar si se quiere ver el mapeo de palabras reservadas:: reservedWordMap.showMap();
        SourceManager sourceManager = new SourceManagerImpl();
        try {
            sourceManager.open(sourceFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error al intentar abrir el archivo fuente.");
        }

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager, reservedWordMap);
        Token token, dummyToken = new Token("", "", 0);

        do {
            try {
                token = lexicalAnalyzer.nextToken();
            } catch (LexicalException lexicalException) {
                System.out.println(lexicalException.getMessage());
                lexicalAnalyzer.registerLexicalError();
                token = dummyToken;
            }

            if (!token.getTokenName().equals(""))
                System.out.println(Formatter.formatToken(token));
        } while (!token.getTokenName().equals("EOF"));

        if (lexicalAnalyzer.getSinErrores()) {
            System.out.println("[SinErrores]");
        }
    }
}