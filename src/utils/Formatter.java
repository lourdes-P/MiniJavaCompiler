package utils;

import lexicalAnalyzer.Token;

public class Formatter {



    public static String blankGenerator(int numberOfSpaces) {
        String spaces ="";
        for (int i=0; i<(numberOfSpaces); i++) {
            spaces+= " ";
        }
        return spaces;
    }

    public static String formatToken(Token token) {
        return "(" + token.getTokenName() + ", " + token.getLexeme() + ", " + token.getLineNumber() + ")";
    }


}