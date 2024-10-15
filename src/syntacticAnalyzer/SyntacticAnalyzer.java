package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.exceptions.LexicalException;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.*;
import semanticAnalyzer.symbolTable.Class;
import semanticAnalyzer.symbolTable.type.PrimitiveType;
import semanticAnalyzer.symbolTable.type.ReferenceType;
import semanticAnalyzer.symbolTable.type.Type;
import semanticAnalyzer.symbolTable.variables.Attribute;
import semanticAnalyzer.symbolTable.variables.Parameter;
import syntacticAnalyzer.exceptions.AbstractSyntacticException;
import syntacticAnalyzer.exceptions.NoMatchSyntacticException;
import syntacticAnalyzer.exceptions.SyntacticException;
import utils.FirstsManager;
import utils.MapManager;
import utils.NextsManager;

import java.util.ArrayList;
import java.util.List;

public class SyntacticAnalyzer {
    private boolean sinErrores;
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;
    private MapManager firstsMap, nextsMap;
    private SymbolTable symbolTable;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer, SymbolTable symbolTable) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        firstsMap = new FirstsManager();
        nextsMap = new NextsManager();
        sinErrores = true;
        this.symbolTable = symbolTable;
    }


    private void match(String expectedTokenName) throws AbstractSyntacticException, LexicalException {
        if(expectedTokenName.equals(currentToken.getTokenName())) {
            currentToken = lexicalAnalyzer.nextToken();
        } else
            throw new NoMatchSyntacticException(currentToken, expectedTokenName);
    }

    public void start() throws AbstractSyntacticException, LexicalException, SemanticException {
        currentToken = lexicalAnalyzer.nextToken();
        if (firstsMap.containsEntry("Start", currentToken.getTokenName())) {
            classList();
        } else if (currentToken.getTokenName().equals("EOF")){
            // end
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("Start"));
        }
    }

    private void classList() throws AbstractSyntacticException, LexicalException, SemanticException {
        if(firstsMap.containsEntry("ClassList", currentToken.getTokenName())) {
            class_();
            classList();
        } else if (nextsMap.containsEntry("ClassList", currentToken.getTokenName())){
            // empty. Token is in the next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ClassList"));
        }
    }

    private void class_() throws AbstractSyntacticException, LexicalException, SemanticException {
        Token class_;
        if (firstsMap.containsEntry("Class", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("pr_class")) {
                match("pr_class");
                class_ = currentToken;
                match("idClase");
                symbolTable.addClass(new Class(class_));
                optionalGenericClassDeclaration();
                Token ancestorToken = optionalInheritance();
                symbolTable.addInheritanceToCurrentClass(ancestorToken);
                match("LlaveAbre");
                memberList();
                match("LlaveCierra");
            } else if (currentToken.getTokenName().equals("pr_abstract")) {
                match("pr_abstract");
                match("pr_class");
                class_ = currentToken;
                match("idClase");
                symbolTable.addClass(new Class(class_));
                optionalGenericClassDeclaration();
                Token ancestorToken = optionalInheritance();
                symbolTable.addInheritanceToCurrentClass(ancestorToken);
                match("LlaveAbre");
                abstractMemberList();
                match("LlaveCierra");
            }
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ClassList"));
        }
    }

    private void optionalGenericClassDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalGenericClassDeclaration", currentToken.getTokenName())) {
            match("Menor");
            match("idClase");
            optionalGenericClassDeclaration();
            continueOptionalGenericClassDeclaration();
            match("Mayor");
        } else if (nextsMap.containsEntry("OptionalGenericClassDeclaration", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalGenericClassDeclaration"));
        }
    }

    private void continueOptionalGenericClassDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueOptionalGenericClassDeclaration", currentToken.getTokenName())) {
            match("Coma");
            continueGenericClassDeclaration();
        } else if (nextsMap.containsEntry("ContinueOptionalGenericClassDeclaration", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ContinueOptionalGenericClassDeclaration"));
        }
    }

    private void continueGenericClassDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueGenericClassDeclaration", currentToken.getTokenName())) {
            match("idClase");
            if(currentToken.getTokenName().equals("Coma")) {
                continueOptionalGenericClassDeclaration();
            } else {
                optionalGenericClassDeclaration();
            }
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ContinueGenericClassDeclaration"));
        }
    }

    private void abstractMemberList() throws AbstractSyntacticException, LexicalException, SemanticException {
        if (firstsMap.containsEntry("AbstractMethod", currentToken.getTokenName())) {
            abstractMethod();
            abstractMemberList();
        } else if (firstsMap.containsEntry("Member", currentToken.getTokenName())) {
            member();
            abstractMemberList();
        }
    }

    private void abstractMethod() throws AbstractSyntacticException, LexicalException, SemanticException {
        match("pr_abstract");
        memberType();
        match("idMetVar");
        formalArguments();
        match("PuntoYComa");
    }

    private Token optionalInheritance() throws AbstractSyntacticException, LexicalException, SemanticException {
        if (firstsMap.containsEntry("OptionalInheritance", currentToken.getTokenName())) {
            match("pr_extends");
            Token inheritFrom = currentToken;
            match("idClase");
            return inheritFrom;
        } else if (nextsMap.containsEntry("OptionalInheritance", currentToken.getTokenName())) {
            // empty. Token is in the next list.
            return PredefinedClassCreator.getObjectClass().getToken();
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalInheritance"));
        }
    }

    private void memberList() throws AbstractSyntacticException, LexicalException, SemanticException {
        if (firstsMap.containsEntry("MemberList", currentToken.getTokenName())) {
            member();
            memberList();
        } else if (nextsMap.containsEntry("MemberList", currentToken.getTokenName())) {
            // empty. Token is in the next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("MemberList"));
        }
    }

    private void member() throws AbstractSyntacticException, LexicalException, SemanticException {
        if (firstsMap.containsEntry("Member", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("pr_public")) {
                constructor();
            } else {
                boolean isStatic = optionalStatic();
                Type type = memberType();
                Token name = currentToken;
                match("idMetVar");
                attributeMethod(isStatic, type, name);
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("Member"));
        }
    }

    private void constructor() throws AbstractSyntacticException, LexicalException, SemanticException {
        match("pr_public");
        Constructor constructor = new Constructor(currentToken, symbolTable.getCurrentClass());
        symbolTable.addConstructorToCurrentClass(constructor);
        match("idClase");
        formalArguments();
        block();
    }

    private void formalArguments() throws AbstractSyntacticException, LexicalException, SemanticException {
        match("ParentesisAbre");
        optionalFormalArgumentList();
        match("ParentesisCierra");
    }

    private void optionalFormalArgumentList() throws AbstractSyntacticException, LexicalException, SemanticException {
        if (firstsMap.containsEntry("OptionalFormalArgumentList", currentToken.getTokenName())) {
            ArrayList<Parameter> parameterList = new ArrayList<>();
            formalArgumentList(parameterList);
            symbolTable.addParameterListToCurrentMethod(parameterList);
        } else if (nextsMap.containsEntry("OptionalFormalArgumentList", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalFormalArgumentList"));
        }
    }

    private void formalArgumentList(ArrayList<Parameter> parameterList) throws AbstractSyntacticException, LexicalException {
        Parameter parameter = formalArgument();
        parameterList.add(parameter);
        parameter.setPositionInMethodParameterList(parameterList.size()-1);
        stopOrContinueFAL(parameterList);
    }

    private Parameter formalArgument() throws AbstractSyntacticException, LexicalException {
        Type type = type();
        Parameter parameter = new Parameter(currentToken, type, symbolTable.getCurrentMethod());
        match("idMetVar");
        return parameter;
    }

    private void stopOrContinueFAL(ArrayList<Parameter> parameterList) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("StopOrContinueFAL", currentToken.getTokenName())) {
            match("Coma");
            formalArgumentList(parameterList);
        } else if (nextsMap.containsEntry("StopOrContinueFAL", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("StopOrContinueFAL"));
        }
    }

    private Type memberType() throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("MemberType", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("pr_void")) {
                Token voidToken = currentToken;
                match("pr_void");
                return new PrimitiveType(voidToken);
            } else {
                return type();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("MemberType"));
        }
    }

    private Type type() throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("Type", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("idClase")) {
                Type type = new ReferenceType(currentToken);
                match("idClase");
                optionalGenericDeclaration();
                return type;
            } else {
                return primitiveType();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("Type"));
        }

    }

    private void optionalGenericDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalGenericDeclaration", currentToken.getTokenName())) {
            match("Menor");
            match("idClase");
            optionalGenericDeclaration();
            continueOptionalGenericDeclaration();
            match("Mayor");
        } else if (nextsMap.containsEntry("OptionalGenericDeclaration", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalGenericDeclaration"));
        }
    }

    private void continueOptionalGenericDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueOptionalGenericDeclaration", currentToken.getTokenName())) {
            match("Coma");
            continueGenericDeclaration();
        } else if (nextsMap.containsEntry("ContinueOptionalGenericDeclaration", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ContinueOptionalGenericDeclaration"));
        }
    }

    private void continueGenericDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueGenericDeclaration", currentToken.getTokenName())) {
            match("idClase");
            if(currentToken.getTokenName().equals("Coma")) {
                continueOptionalGenericDeclaration();
            } else {
                optionalGenericDeclaration();
            }
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ContinueGenericDeclaration"));
        }
    }

    private PrimitiveType primitiveType() throws AbstractSyntacticException, LexicalException {
        Token type = currentToken;
        if (currentToken.getTokenName().equals("pr_boolean")) {
            match("pr_boolean");
            return new PrimitiveType(type);
        } else if (currentToken.getTokenName().equals("pr_char")) {
            match("pr_char");
            return new PrimitiveType(type);
        } else if (currentToken.getTokenName().equals("pr_int")) {
            match("pr_int");
            return new PrimitiveType(type);
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("PrimitiveType"));
        }
    }

    private boolean optionalStatic() throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("OptionalStatic", currentToken.getTokenName())) {
            match("pr_static");
            return true;
        } else if (nextsMap.containsEntry("OptionalStatic", currentToken.getTokenName())) {
            // empty. Token is in next list.
            return false;
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalStatic"));
        }
    }

    private void attributeMethod(boolean isStatic, Type type, Token attrOrMethodToken) throws AbstractSyntacticException, LexicalException, SemanticException {
        if (firstsMap.containsEntry("AttributeMethod", currentToken.getTokenName())) {
            if (firstsMap.containsEntry("Attribute", currentToken.getTokenName())) {
                attribute();
                Attribute attribute = new Attribute(attrOrMethodToken, type, symbolTable.getCurrentClass(), isStatic);
                symbolTable.addAttributeToCurrentClass(attribute);
                match("PuntoYComa");
            } else {
                Method method = new Method(isStatic, attrOrMethodToken, symbolTable.getCurrentClass(), type);
                symbolTable.addMethodToCurrentClass(method);
                formalArguments();
                block();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("AttributeMethod"));
        }
    }

    private void attribute() throws AbstractSyntacticException, LexicalException {
        optionalAttributeInitialization();
        continueAttributeDeclaration();
    }

    private void optionalAttributeInitialization() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalAttributeInitialization", currentToken.getTokenName())) {
            match("Asignacion");
            composedExpression();
        } else if (nextsMap.containsEntry("OptionalAttributeInitialization", currentToken.getTokenName())) {
            //empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalAttributeInitialization"));
        }
     }

     private void continueAttributeDeclaration() throws AbstractSyntacticException, LexicalException {
         if (firstsMap.containsEntry("ContinueAttributeDeclaration", currentToken.getTokenName())) {
             match("Coma");
             match("idMetVar");
             attribute();
         } else if (nextsMap.containsEntry("ContinueAttributeDeclaration", currentToken.getTokenName())) {
             //empty. Token is in next list.
         } else {
             throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ContinueAttributeDeclaration"));
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
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("Return",currentToken.getTokenName())) {
                return_();
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("Break", currentToken.getTokenName())) {
                break_();
                match("PuntoYComa");
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
        optionalGenericConstructorInvocation();
        actualArguments();
    }

    private void optionalGenericConstructorInvocation() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalGenericConstructorInvocation", currentToken.getTokenName())) {
            match("Menor");
            optionalDiamondNotation();
        } else if (nextsMap.containsEntry("OptionalGenericConstructorInvocation", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalGenericConstructorInvocation"));
        }
    }

    private void optionalDiamondNotation() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalDiamondNotation", currentToken.getTokenName())) {
            if(currentToken.getTokenName().equals("Mayor")) {
                match("Mayor");
            } else {
                match("idClase");
                optionalGenericDeclaration();
                continueOptionalGenericConstructorInvocation();
                match("Mayor");
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("OptionalDiamondNotation"));
        }
    }

    private void continueOptionalGenericConstructorInvocation() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueOptionalGenericConstructorInvocation", currentToken.getTokenName())) {
            match("Coma");
            continueGenericConstructorInvocation();
        } else if (nextsMap.containsEntry("ContinueOptionalGenericConstructorInvocation", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ContinueOptionalGenericConstructorInvocation"));
        }
    }

    private void continueGenericConstructorInvocation() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueGenericConstructorInvocation", currentToken.getTokenName())) {
            match("idClase");
            if(currentToken.getTokenName().equals("Coma")) {
                continueGenericConstructorInvocation();
            } else {
                optionalGenericDeclaration();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("ContinueGenericConstructorInvocation"));
        }
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
        if (firstsMap.containsEntry("LocalVar", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("pr_var")) {
                localVarVar();
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("Type", currentToken.getTokenName())) {
                type();
                localVarClassic();
                match("PuntoYComa");
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
