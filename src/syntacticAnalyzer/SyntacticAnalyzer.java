package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.exceptions.LexicalException;
import syntacticAnalyzer.exceptions.AbstractSyntacticException;
import syntacticAnalyzer.exceptions.NoMatchSyntacticException;
import syntacticAnalyzer.exceptions.SyntacticException;
import utils.FirstsManager;
import utils.MapManager;
import utils.NextsManager;

import java.util.ArrayList;
import java.util.List;

public class SyntacticAnalyzer {
    private boolean sinErrores, recoverFromError;
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;
    private MapManager firstsMap, nextsMap;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        firstsMap = new FirstsManager();
        nextsMap = new NextsManager();
        sinErrores = true;
    }


    private void match(String expectedTokenName) throws AbstractSyntacticException, LexicalException {
        if(expectedTokenName.equals(currentToken.getTokenName())) {
            currentToken = lexicalAnalyzer.nextToken();
        } else
            throw new NoMatchSyntacticException(currentToken, expectedTokenName);

        // TODO el main antes: } while (!token.getTokenName().equals("EOF"));
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

    public void start() throws AbstractSyntacticException, LexicalException {
        currentToken = lexicalAnalyzer.nextToken();
        if (firstsMap.containsEntry("Start", currentToken.getTokenName())) {
            classList();
        } else if (currentToken.getTokenName().equals("EOF")){
            // end
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("Start"));
        }
    }

    private void classList() throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("ClassList", currentToken.getTokenName())) {
            class_();
            classList();
        } else if (nextsMap.containsEntry("ClassList", currentToken.getTokenName())){
            // empty. Token is in the next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ClassList"));
        }
    }

    private void class_() throws AbstractSyntacticException, LexicalException {
        match("pr_class");
        match("idClase");
        optionalInheritance();
        match("LlaveAbre");
        memberList();
        match("LlaveCierra");
    }

    private void optionalInheritance() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalInheritance", currentToken.getTokenName())) {
            match("pr_extends");
            match("idClase");
        } else if (nextsMap.containsEntry("OptionalInheritance", currentToken.getTokenName())) {
            // empty. Token is in the next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalInheritance"));
        }
    }

    private void memberList() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("MemberList", currentToken.getTokenName())) {
            member();
            memberList();
        } else if (nextsMap.containsEntry("MemberList", currentToken.getTokenName())) {
            // empty. Token is in the next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("MemberList"));
        }
    }

    private void member() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("Member", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("pr_public")) {
                constructor();
            } else {
                optionalStatic();
                memberType();
                match("idMetVar");
                attributeMethod();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("Member"));
        }
    }

    private void constructor() throws AbstractSyntacticException, LexicalException {
        match("pr_public");
        match("idClase");
        formalArguments();
        block();
    }

    private void formalArguments() throws AbstractSyntacticException, LexicalException {
        match("ParentesisAbre");
        optionalFormalArgumentList();
        match("ParentesisCierra");
    }

    private void optionalFormalArgumentList() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalFormalArgumentList", currentToken.getTokenName())) {
            formalArgumentList();
        } else if (nextsMap.containsEntry("OptionalFormalArgumentList", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalFormalArgumentList"));
        }
    }

    private void formalArgumentList() throws AbstractSyntacticException, LexicalException {
        formalArgument();
        stopOrContinueFAL();
    }

    private void formalArgument() throws AbstractSyntacticException, LexicalException {
        type();
        match("idMetVar");
    }

    private void stopOrContinueFAL() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("StopOrContinueFAL", currentToken.getTokenName())) {
            match("Coma");
            formalArgumentList();
        } else if (nextsMap.containsEntry("StopOrContinueFAL", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("StopOrContinueFAL"));
        }
    }

    private void memberType() throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("MemberType", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("pr_void")) {
                match("pr_void");
            } else {
                type();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("MemberType"));
        }
    }

    private void type() throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("Type", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("idClase")) {
                match("idClase");
            } else {
                primitiveType();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("Type"));
        }
    }

    private void primitiveType() throws AbstractSyntacticException, LexicalException {
        if (currentToken.getTokenName().equals("pr_boolean")) {
            match("pr_boolean");
        } else if (currentToken.getTokenName().equals("pr_char")) {
            match("pr_char");
        } else if (currentToken.getTokenName().equals("pr_int")) {
            match("pr_int");
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("PrimitiveType"));
        }
    }

    private void optionalStatic() throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("OptionalStatic", currentToken.getTokenName())) {
            match("pr_static");
        } else if (nextsMap.containsEntry("OptionalStatic", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalStatic"));
        }
    }

    private void attributeMethod() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("AttributeMethod", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("PuntoYComa")) {
                match("PuntoYComa");
            } else if(currentToken.getTokenName().equals("Asignacion")) {
                match("Asignacion");
                expression();
                match("PuntoYComa");
            } else {
                formalArguments();
                block();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("AttributeMethod"));
        }
    }

    private void block() throws AbstractSyntacticException, LexicalException {
        match("LlaveAbre");
        sentenceList();
        match("LlaveCierra");
    }

    private void sentenceList() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("SentenceList", currentToken.getTokenName())) {
            sentence();
            sentenceList();
        } else if (nextsMap.containsEntry("SentenceList", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("SentenceList"));
        }
    }

    private void sentence() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("Sentence", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("PuntoYComa")) {
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("Block", currentToken.getTokenName())) {
                block();
            } else if (firstsMap.containsEntry("AssignmentOrCall", currentToken.getTokenName())) {
                assignmentOrCall();
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("LocalVar", currentToken.getTokenName())) {
                localVar();
            } else if (firstsMap.containsEntry("Return",currentToken.getTokenName())) {
                return_();
            } else if (firstsMap.containsEntry("Break", currentToken.getTokenName())) {
                break_();
            } else if (firstsMap.containsEntry("If", currentToken.getTokenName())) {
                if_();
            } else if (firstsMap.containsEntry("While", currentToken.getTokenName())) {
                while_();
            } else if (firstsMap.containsEntry("Switch", currentToken.getTokenName())) {
                switch_();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("Sentence"));
        }
    }

    private void assignmentOrCall() throws AbstractSyntacticException, LexicalException {
        expression();
    }

    private void expression() throws AbstractSyntacticException, LexicalException {
        composedExpression();
        assignmentOrEndOfExpression();
    }

    private void composedExpression() throws AbstractSyntacticException, LexicalException {
        basicExpression();
        continueComposedExpression();
    }

    private void basicExpression() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("BasicExpression", currentToken.getTokenName())) {
            if (firstsMap.containsEntry("UnaryOperator", currentToken.getTokenName())) {
                unaryOperator();
                operand();
            } else {
                operand();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("BasicExpression"));
        }
    }

    private void unaryOperator() throws AbstractSyntacticException, LexicalException {
        switch (currentToken.getTokenName()) {
            case "Mas" -> match("Mas");
            case "Menos" -> match("Menos");
            case "Not" -> match("Not");
            default -> throw new SyntacticException(currentToken, firstsMap.getValue("UnaryOperator")); // not reachable
        }
    }

    private void operand() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("Operand", currentToken.getTokenName())) {
            if (firstsMap.containsEntry("Literal", currentToken.getTokenName())) {
                literal();
            } else {
                access();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("Operand"));
        }
    }

    private void literal() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("Literal", currentToken.getTokenName())) {
            if (firstsMap.containsEntry("PrimitiveLiteral", currentToken.getTokenName())) {
                primitiveLiteral();
            } else {
                objectLiteral();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("Literal"));
        }
    }

    private void primitiveLiteral() throws AbstractSyntacticException, LexicalException {
        switch (currentToken.getTokenName()) {
            case "pr_true" -> match("pr_true");
            case "pr_false" -> match("pr_false");
            case "intLiteral" -> match("intLiteral");
            case "charLiteral" -> match("charLiteral");
            default ->
                    throw new SyntacticException(currentToken, firstsMap.getValue("PrimitiveLiteral"));
        }
    }

    private void objectLiteral() throws AbstractSyntacticException, LexicalException {
        switch (currentToken.getTokenName()) {
            case "pr_null" -> match("pr_null");
            case "stringLiteral" -> match("stringLiteral");
            default ->
                    throw new SyntacticException(currentToken, firstsMap.getValue("ObjectLiteral")); // not reachable
        }
    }

    private void access() throws AbstractSyntacticException, LexicalException {
        primary();
        optionalChain();
    }

    private void primary() throws AbstractSyntacticException, LexicalException {
        if (currentToken.getTokenName().equals("idMetVar")) {
            match("idMetVar");
            varOrMethodAccess();
        } else if (firstsMap.containsEntry("ThisAccess", currentToken.getTokenName())) {
            thisAccess();
        } else if (firstsMap.containsEntry("ConstructorAccess", currentToken.getTokenName())) {
            constructorAccess();
        } else if (firstsMap.containsEntry("StaticMethodAccess", currentToken.getTokenName())) {
            staticMethodAccess();
        } else if (firstsMap.containsEntry("ParenthesizedExpression", currentToken.getTokenName())) {
            parenthesizedExpression();
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("Primary"));
        }
    }

    private void varOrMethodAccess() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("VarOrMethodAccess", currentToken.getTokenName())) {
            actualArguments();
        } else if (nextsMap.containsEntry("VarOrMethodAccess", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("VarOrMethodAccess"));
        }
    }

    private void actualArguments() throws AbstractSyntacticException, LexicalException {
        match("ParentesisAbre");
        optionalExpressionList();
        match("ParentesisCierra");
    }

    private void optionalExpressionList() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalExpressionList", currentToken.getTokenName())) {
            expressionList();
        } else if (nextsMap.containsEntry("OptionalExpressionList", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalExpressionList"));
        }
    }

    private void expressionList() throws AbstractSyntacticException, LexicalException {
        expression();
        expressionListContinuation();
    }

    private void expressionListContinuation() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ExpressionListContinuation", currentToken.getTokenName())) {
            match("Coma");
            expressionList();
        } else if (nextsMap.containsEntry("ExpressionListContinuation", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ExpressionListContinuation"));
        }
    }

    private void thisAccess() throws AbstractSyntacticException, LexicalException {
        match("pr_this");
    }

    private void constructorAccess() throws AbstractSyntacticException, LexicalException {
        match("pr_new");
        match("idClase");
        actualArguments();
    }

    private void staticMethodAccess() throws AbstractSyntacticException, LexicalException {
        match("idClase");
        match("Punto");
        match("idMetVar");
        actualArguments();
    }

    private void parenthesizedExpression() throws AbstractSyntacticException, LexicalException {
        match("ParentesisAbre");
        expression();
        match("ParentesisCierra");
    }

    private void optionalChain() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalChain", currentToken.getTokenName())) {
            match("Punto");
            match("idMetVar");
            chainedVarOrMethod();
        } else if (nextsMap.containsEntry("OptionalChain", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalChain"));
        }
    }

    private void chainedVarOrMethod() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ActualArguments", currentToken.getTokenName())) {
            actualArguments();
            optionalChain();
        } else {
            optionalChain();
        }
    }

    private void continueComposedExpression() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueComposedExpression", currentToken.getTokenName())) {
            binaryOperator();
            basicExpression();
            continueComposedExpression();
        } else if (nextsMap.containsEntry("ContinueComposedExpression", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ContinueComposedExpression"));
        }
    }

    private void binaryOperator() throws AbstractSyntacticException, LexicalException {
        switch (currentToken.getTokenName()) {
            case "Or" -> match("Or");
            case "And" -> match("And");
            case "Equals" -> match("Equals");
            case "Differs" -> match("Differs");
            case "Mayor" -> match("Mayor");
            case "MayorIgual" -> match("MayorIgual");
            case "Menor" -> match("Menor");
            case "MenorIgual" -> match("MenorIgual");
            case "Mas" -> match("Mas");
            case "Menos" -> match("Menos");
            case "Multiplicador" -> match("Multiplicador");
            case "Divisor" -> match("Divisor");
            case "Porcentaje" -> match("Porcentaje");
            default ->
                    throw new SyntacticException(currentToken, firstsMap.getValue("BinaryOperator"));
        }
    }

    private void localVar() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("localVar", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("pr_var")) {
                localVarVar();
            } else if (firstsMap.containsEntry("Type", currentToken.getTokenName())) {
                localVarClassic();
            }
        }
    }

    private void localVarVar() throws AbstractSyntacticException, LexicalException {
        match("pr_var");
        match("idMetVar");
        match("Asignacion");
        composedExpression();
    }

    private void localVarClassic() throws AbstractSyntacticException, LexicalException {
        type();
        match("idMetVar");
        optionalClassicVarInitialization();
        continueLocalVarDeclaration();
    }

    private void optionalClassicVarInitialization() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalClassicVarInitialization", currentToken.getTokenName())) {
            match("Asignacion");
            composedExpression();
        } else if (nextsMap.containsEntry("OptionalClassicVarInitialization", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalClassicVarInitialization"));
        }
    }

    private void continueLocalVarDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueLocalVarDeclaration", currentToken.getTokenName())) {
            match("Coma");
            localVarClassic();
        } else if (nextsMap.containsEntry("ContinueLocalVarDeclaration", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ContinueLocalVarDeclaration"));
        }
    }

    private void return_() throws AbstractSyntacticException, LexicalException {
        match("pr_return");
        optionalExpression();
    }

    private void optionalExpression() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalExpression", currentToken.getTokenName())) {
            expression();
        } else if (nextsMap.containsEntry("OptionalExpression", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalExpression"));
        }
    }

    private void break_() throws AbstractSyntacticException, LexicalException {
        match("pr_break");
    }

    private void if_() throws AbstractSyntacticException, LexicalException {
        match("pr_if");
        match("ParentesisAbre");
        expression();
        match("ParentesisCierra");
        sentence();
        else_();
    }

    private void else_() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("Else", currentToken.getTokenName())) {
            match("pr_else");
            sentence();
        } else if (nextsMap.containsEntry("Else", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("Else"));
        }
    }

    private void while_() throws AbstractSyntacticException, LexicalException {
        match("pr_while");
        match("ParentesisAbre");
        expression();
        match("ParentesisCierra");
        sentence();
    }

    private void switch_() throws AbstractSyntacticException, LexicalException {
        match("pr_switch");
        match("ParentesisAbre");
        expression();
        match("ParentesisCierra");
        match("LlaveAbre");
        switchSentenceList();
        match("LlaveCierra");
    }

    private void switchSentenceList() throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("SwitchSentenceList", currentToken.getTokenName())) {
            switchSentence();
            switchSentenceList();
        } else if (nextsMap.containsEntry("SwitchSentenceList", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("SwitchSentenceList"));
        }
    }

    private void switchSentence() throws AbstractSyntacticException, LexicalException {
        if (currentToken.getTokenName().equals("pr_case")) {
            match("pr_case");
            primitiveLiteral();
            match("DosPuntos");
            optionalSentence();
        } else if (currentToken.getTokenName().equals("pr_default")) {
            match("pr_default");
            match("DosPuntos");
            sentence();
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("SwitchSentenceList"));
        }
    }

    private void optionalSentence() throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("OptionalSentence", currentToken.getTokenName())) {
            sentence();
        } else if (nextsMap.containsEntry("OptionalSentence", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalSentence"));
        }
    }

    private void assignmentOrEndOfExpression() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("AssignmentOrEndOfExpression", currentToken.getTokenName())) {
            assignOperator();
            composedExpression();
        } else if (nextsMap.containsEntry("AssignmentOrEndOfExpression", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("AssignmentOrEndOfExpression"));
        }
    }

    private void assignOperator() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("AssignOperator", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("Asignacion")) {
                match("Asignacion");
            } else if (currentToken.getTokenName().equals("AsignacionSuma")) {
                match("AsignacionSuma");
            } else {
                match("AsignacionResta");
            }
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("AssignOperator"));
        }
    }

    private List<String> concatenateFirstListAndNextList(String productionName) {
        List<String> firstAndNextList = new ArrayList<>(firstsMap.getValue(productionName));
        firstAndNextList.addAll(nextsMap.getValue(productionName));

        return firstAndNextList;
    }

    public void registerSyntacticError() {
        sinErrores = false;
    }

    public boolean getSinErrores() {
        return sinErrores;
    }
}
