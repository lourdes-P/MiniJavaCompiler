package lexicalAnalyzer;

import ioManager.SourceManager;
import lexicalAnalyzer.exceptions.*;
import lexicalAnalyzer.reservedWordManager.ReservedWordMap;

import java.io.IOException;


public class LexicalAnalyzer {
    private String lexeme;
    private char currentChar;
    private boolean sinErrores, recoverFromError;
    private SourceManager sourceManager;
    private ReservedWordMap reservedWordMap;

    public LexicalAnalyzer(SourceManager sourceManager, ReservedWordMap reservedWordMap) {
        this.sourceManager = sourceManager;
        this.reservedWordMap = reservedWordMap;
        sinErrores = true;
        recoverFromError = false;
        updateCurrentChar();
    }

    public Token nextToken() throws LexicalException {
        if (recoverFromError) {
            updateCharToNextBlank();
            recoverFromError = false;
        }
        lexeme = "";
        return e0();
    }

    private void eraseLexeme() {
        lexeme = "";
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

    private void updateCharToNextBlank() {
        while (!reachedEOF() && !Character.isWhitespace(currentChar)) {
            updateCurrentChar();
        }
    }

    private Token e0() throws LexicalException {
        if (Character.isWhitespace(currentChar)) {
            updateCurrentChar();
            return e0();
        } else if (currentChar == '/') {
            updateLexeme();
            updateCurrentChar();
            return eDivisionOp();
        } else if (Character.isLowerCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eIdMetVar();
        } else if (currentChar == '*') {
            updateLexeme();
            updateCurrentChar();
            return eMultiplierOp();
        } else if (currentChar == '%') {
            updateLexeme();
            updateCurrentChar();
            return ePercentageOp();
        } else if (currentChar == '!') {
            updateLexeme();
            updateCurrentChar();
            return eNotOp();
        } else if (currentChar == '>') {
            updateLexeme();
            updateCurrentChar();
            return eGreaterThanOp();
        } else if(currentChar == '<') {
            updateLexeme();
            updateCurrentChar();
            return eLesserThanOp();
        } else if (currentChar == '=') {
            updateLexeme();
            updateCurrentChar();
            return eAssign();
        } else if (currentChar == '+') {
            updateLexeme();
            updateCurrentChar();
            return ePlusOp();
        } else if (currentChar == '-'){
            updateLexeme();
            updateCurrentChar();
            return eMinusOp();
        } else if (currentChar == ';') {
            updateLexeme();
            updateCurrentChar();
            return eSemicolonPunct();
        } else if (currentChar == ',') {
            updateLexeme();
            updateCurrentChar();
            return eCommaPunct();
        } else if (currentChar == '.') {
            updateLexeme();
            updateCurrentChar();
            return ePeriodPunct();
        } else if (currentChar == ':') {
            updateLexeme();
            updateCurrentChar();
            return eColonPunct();
        } else if (currentChar == '(') {
            updateLexeme();
            updateCurrentChar();
            return eOpenParenthesisPunct();
        } else if (currentChar == ')') {
            updateLexeme();
            updateCurrentChar();
            return eCloseParenthesisPunct();
        } else if (currentChar == '{') {
            updateLexeme();
            updateCurrentChar();
            return eOpenBracePunct();
        } else if (currentChar == '}') {
            updateLexeme();
            updateCurrentChar();
            return eCloseBracePunct();
        } else if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eInt1();
        } else if (currentChar == '\'') {
            updateLexeme();
            updateCurrentChar();
            return eCharacter();
        } else if (currentChar == '"'){
            updateLexeme();
            updateCurrentChar();
            return eStringOpen();
        } else if (currentChar == '&') {
            updateLexeme();
            updateCurrentChar();
            return eAndUnfinished();
        } else if (currentChar == '|') {
            updateLexeme();
            updateCurrentChar();
            return eOrUnfinished();
        } else if (Character.isUpperCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eIdClass();
        } else if (currentChar == SourceManager.END_OF_FILE) {
            return eEOF();
        } else {
            updateLexeme();
            throw new LexicalExceptionInvalidSymbol(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        }
    }




    private Token eAndUnfinished() throws LexicalException {
        if(currentChar == '&') {
            updateLexeme();
            updateCurrentChar();
            return eAndOp();
        } else if (!Character.isWhitespace(currentChar) && !reachedEOF()){
            updateLexeme();
            throw new LexicalExceptionLogicOperators(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        } else {
            throw new LexicalExceptionLogicOperators(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        }
    }

    private Token eOrUnfinished() throws LexicalException {
        if(currentChar == '|') {
            updateLexeme();
            updateCurrentChar();
            return eOrOp();
        }
        else if (!Character.isWhitespace(currentChar) && !reachedEOF()) {
            updateLexeme();
            throw new LexicalExceptionLogicOperators(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        } else {
            throw new LexicalExceptionLogicOperators(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        }
    }

    private Token eStringOpen() throws LexicalException {
        if (currentChar == '"') {
            updateLexeme();
            updateCurrentChar();
            return eStringFinished();
        } else if (currentChar == '\\') {
            updateLexeme();
            updateCurrentChar();
            return eStringOpenedSlash();
        } else if (!isEnter(currentChar) && !reachedEOF()) {
            updateLexeme();
            updateCurrentChar();
            return eStringOpen();
        } else {
            throw new LexicalExceptionStringMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        }
    }

    private Token eStringOpenedSlash() throws LexicalException {
        if(!Character.isWhitespace(currentChar) && !reachedEOF()) {
            updateLexeme();
            updateCurrentChar();
            return eStringOpen();
        } else if (!isEnter(currentChar) && !reachedEOF()){
            updateLexeme();
            throw new LexicalExceptionStringMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        } else {
            throw new LexicalExceptionStringMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        }
    }

    private Token eCommentLine() throws LexicalException {
        if (isEnter(currentChar) || reachedEOF()) {
            updateCurrentChar();
            return e0();
        } else {
            updateCurrentChar();
            return eCommentLine();
        }
    }

    private Token eOpenComment() throws LexicalException {
        if(currentChar == '*') {
            updateLexeme();
            updateCurrentChar();
            return eOpenComment2();
        } else if (reachedEOF()) {
            throw new LexicalExceptionCommentMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        } else if(isEnter(currentChar)) {
            eraseLexeme();
            updateCurrentChar();
            return eOpenComment();
        } else {
            updateLexeme();
            updateCurrentChar();
            return eOpenComment();
        }

    }

    private Token eOpenComment2() throws LexicalException {
        if (currentChar == '/') {
            eraseLexeme();
            updateCurrentChar();
            return e0();
        } else if (currentChar == '*') {
            updateCurrentChar();
            return eOpenComment2();
        } else if (reachedEOF()) {
            throw new LexicalExceptionCommentMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        } else if (isEnter(currentChar)) {
            eraseLexeme();
            updateCurrentChar();
            return eOpenComment();
        } else {
            updateCurrentChar();
            return eOpenComment();
        }
    }

    private Token eCharacter() throws LexicalException {
        if(currentChar =='\\') {
            updateLexeme();
            updateCurrentChar();
            return eCharacterSlash();
        } else if (currentChar == '\'') {
            updateLexeme();
            throw new LexicalExceptionCharacterMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        }else if (!isEnter(currentChar) && !reachedEOF()) {
            updateLexeme();
            updateCurrentChar();
            return eCharacter2();
        } else {
            throw new LexicalExceptionCharacterMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        }
    }

    private Token eCharacterSlash() throws LexicalException {
        if ((!Character.isWhitespace(currentChar) || currentChar==' ') && !reachedEOF()) {
            updateLexeme();
            updateCurrentChar();
            return eCharacter2();
        } else if(!isEnter(currentChar) && !reachedEOF()){
            updateLexeme();
            throw new LexicalExceptionCharacterMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        } else {
            throw new LexicalExceptionCharacterMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        }
    }

    private Token eCharacter2() throws LexicalException {
        if(currentChar == '\'') {
            updateLexeme();
            updateCurrentChar();
            return eCharacterLiteral();
        } else if (!isEnter(currentChar) && !reachedEOF()){
            updateLexeme();
            throw new LexicalExceptionCharacterMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        } else {
            throw new LexicalExceptionCharacterMalformed(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        }
    }


    // --- finisher states ---

    private Token eOpenBracePunct() throws LexicalException {
        return new Token ("LlaveAbre", lexeme, sourceManager.getLineNumber());
    }

    private Token eCloseBracePunct() throws LexicalException {
        return new Token ("LlaveCierra", lexeme, sourceManager.getLineNumber());
    }

    private Token eOpenParenthesisPunct() throws LexicalException {
        return new Token ("ParentesisAbre", lexeme, sourceManager.getLineNumber());
    }

    private Token eCloseParenthesisPunct() throws LexicalException {
        return new Token ("ParentesisCierra", lexeme, sourceManager.getLineNumber());
    }

    private Token ePeriodPunct() throws LexicalException {
        return new Token ("Punto", lexeme, sourceManager.getLineNumber());
    }

    private Token eCommaPunct() throws LexicalException {
        return new Token ("Coma", lexeme, sourceManager.getLineNumber());
    }

    private Token eSemicolonPunct() throws LexicalException {
        return new Token ("PuntoYComa", lexeme, sourceManager.getLineNumber());
    }

    private Token eColonPunct() throws LexicalException {
        return new Token ("DosPuntos", lexeme, sourceManager.getLineNumber());
    }

    private Token ePercentageOp() throws LexicalException {
        return new Token("Porcentaje", lexeme, sourceManager.getLineNumber());
    }

    private Token eMultiplierOp() throws LexicalException {
        return new Token("Multiplicador", lexeme, sourceManager.getLineNumber());
    }

    private Token eDivisionOp() throws LexicalException {
        if (currentChar == '*') {
            updateLexeme();
            updateCurrentChar();
            return eOpenComment();
        } else if (currentChar == '/') {
            eraseLexeme();
            updateCurrentChar();
            return eCommentLine();
        } else {
            return new Token("Divisor", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token ePlusOp() throws LexicalException {
        if (currentChar != '=') {
            return new Token("Mas", lexeme, sourceManager.getLineNumber());
        } else {
            updateLexeme();
            updateCurrentChar();
            return eAssignPlus();
        }
    }

    private Token eMinusOp() throws LexicalException {
        if (currentChar != '=') {
            return new Token("Menos", lexeme, sourceManager.getLineNumber());
        } else {
            updateLexeme();
            updateCurrentChar();
            return eAssignMinus();
        }
    }

    private Token eNotOp() throws LexicalException {
        if(currentChar != '=') {
            return new Token("Not", lexeme, sourceManager.getLineNumber());
        } else {
            updateLexeme();
            updateCurrentChar();
            return eDifferOp();
        }
    }

    private Token eDifferOp() throws LexicalException {
        return new Token("Differs", lexeme, sourceManager.getLineNumber());
    }

    private Token eGreaterThanOp() throws LexicalException {
        if(currentChar != '=') {
            return new Token("Mayor", lexeme, sourceManager.getLineNumber());
        } else {
            updateLexeme();
            updateCurrentChar();
            return eGreaterOrEqualToOp();
        }
    }

    private Token eGreaterOrEqualToOp() throws LexicalException {
        return new Token("MayorIgual", lexeme, sourceManager.getLineNumber());
    }

    private Token eLesserThanOp() throws LexicalException {
        if(currentChar != '=') {
            return new Token("Menor", lexeme, sourceManager.getLineNumber());
        } else {
            updateLexeme();
            updateCurrentChar();
            return eLesserOrEqualToOp();
        }
    }

    private Token eLesserOrEqualToOp() throws LexicalException {
        return new Token("MenorIgual", lexeme, sourceManager.getLineNumber());
    }

    private Token eAndOp() throws LexicalException {
        return new Token ("And", lexeme, sourceManager.getLineNumber());
    }

    private Token eOrOp() throws LexicalException {
        return new Token("Or", lexeme, sourceManager.getLineNumber());
    }

    private Token eAssign() throws LexicalException {
        if (currentChar != '=') {
            return new Token("Asignacion", lexeme, sourceManager.getLineNumber());
        } else {
            updateLexeme();
            updateCurrentChar();
            return eEqualsOp();
        }
    }

    private Token eEqualsOp() throws LexicalException {
        return new Token ("Equals", lexeme, sourceManager.getLineNumber());
    }

    private Token eAssignPlus() throws LexicalException {
        return new Token ("AsignacionSuma", lexeme, sourceManager.getLineNumber());
    }

    private Token eAssignMinus() throws LexicalException {
        return new Token("AsignacionResta", lexeme, sourceManager.getLineNumber());
    }

    private Token eIdClass() throws LexicalException {
        if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
            updateLexeme();
            updateCurrentChar();
            return eIdClass();
        } else {
            return new Token("idClase", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eStringFinished() throws LexicalException {
        return new Token("stringLiteral", lexeme, sourceManager.getLineNumber());
    }

    private Token eIdMetVar() throws LexicalException {
        if(Character.isLowerCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eIdMetVarOrReservedWord();
        } else if (Character.isUpperCase(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
            updateLexeme();
            updateCurrentChar();
            return eIdMetVar2();
        } else {
            return new Token ("idMetVar", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eIdMetVar2() throws LexicalException{
        if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
            updateLexeme();
            updateCurrentChar();
            return eIdMetVar2();
        } else {
            return new Token ("idMetVar", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eIdMetVarOrReservedWord() throws LexicalException {
        if(Character.isLowerCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eIdMetVarOrReservedWord();
        } else if (Character.isUpperCase(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
            updateLexeme();
            updateCurrentChar();
            return eIdMetVar2();
        } else {
            if (reservedWordMap.isReservedWord(lexeme))
                return new Token (reservedWordMap.getReservedWordID(lexeme), lexeme, sourceManager.getLineNumber());
            else
                return new Token ("idMetVar", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eInt1() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eInt2();
        } else {
            return new Token ("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eInt2() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eInt3();
        } else {
            return new Token ("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eInt3() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eInt4();
        } else {
            return new Token ("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eInt4() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eInt5();
        } else {
            return new Token ("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eInt5() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eInt6();
        } else {
            return new Token ("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eInt6() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eInt7();
        } else {
            return new Token ("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eInt7() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eInt8();
        } else {
            return new Token ("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eInt8() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return eInt9();
        } else {
            return new Token ("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eInt9() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            throw new LexicalExceptionInvalidInteger(lexeme, sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine());
        } else {
            return new Token ("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token eCharacterLiteral() throws LexicalException {
        return new Token ("charLiteral", lexeme, sourceManager.getLineNumber());
    }

    private Token eEOF() throws LexicalException {
        return new Token ("EOF", "", sourceManager.getLineNumber());
    }


    // ------------------------------

    private boolean isEnter(char character) {
        return character == Character.DIRECTIONALITY_PARAGRAPH_SEPARATOR || character == Character.LINE_SEPARATOR;
    }


    // ------------------------------

    public boolean reachedEOF() {
        return currentChar == SourceManager.END_OF_FILE;
    }

    private void resetLexicalAnalizer() {
        sinErrores = true;
        recoverFromError = false;
        updateCurrentChar();
    }

    public void setSourceManager(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
        resetLexicalAnalizer();
    }

    public void registerLexicalError() {
        sinErrores = false;
        recoverFromError = true;
    }

    public boolean getSinErrores() {
        return sinErrores;
    }



}
