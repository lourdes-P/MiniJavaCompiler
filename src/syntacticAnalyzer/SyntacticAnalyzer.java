package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;
import syntacticAnalyzer.exceptions.SyntacticException;

public class SyntacticAnalyzer {
    private String syntacticStructure;
    private boolean sinErrores, recoverFromError;
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;


    private void match(String expectedTokenName) throws Exception {
        // TODO ver como manejar la excepcion del lexico (en este momento es lo mas general posible)
        if(expectedTokenName.equals(currentToken.getTokenName()))
            currentToken = lexicalAnalyzer.nextToken();
        else
            throw new SyntacticException(currentToken, expectedTokenName);
    }

    private void updateTokenAfterError() {
        // TODO
        // Si el error se produjo en una sentencia, debería continuar con la siguiente
        //sentencia o finalizar el bloque (en caso de la errónea ser la última)
        // Si el error se produjo en una expresión se salte el resto de la expresión y
        //se continúe con la sentencia que la contiene
        // Si el error se produce en la encabezado de declaración de un
        //método/constructor se debe continuar con su bloque
        // Si el error se produce en una declaración de atributo se continua con la
        //siguiente declaración.
    }

    public void S() throws SyntacticException {

    }

    private void classList() throws SyntacticException {

    }
}
