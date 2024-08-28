package lexicalAnalyzer;

import ioManager.SourceManager;

import java.io.IOException;

public class LexicalAnalyzer {
    private String lexeme;
    private char currentChar;
    private SourceManager sourceManager;

    public Token nextToken() {
        lexeme = "";
        return e0();
    }

    private void updateLexeme() {
        lexeme = lexeme + currentChar;
    }

    private void updateCurrentChar() {
        try {
            currentChar = sourceManager.getNextChar();
        } catch (IOException e) {
            System.out.println("Source Manager error.");
        }
    }

    private Token e0() {

        return null;
    }
}
