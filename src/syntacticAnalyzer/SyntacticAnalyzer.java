package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.exceptions.LexicalException;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes.AdditionAssignmentExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes.AssignmentExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.assignmentExpressionNodes.SubtractionAssignmentExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.binaryExpressionNodes.*;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.AccessNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.ChainNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal.LiteralNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal.ObjectLiteralNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal.PrimitiveLiteralNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary.*;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes.MinusNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes.NotNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes.PlusNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes.UnaryExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.*;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.*;
import semanticAnalyzer.symbolTable.Class;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.ReferenceType;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Attribute;
import semanticAnalyzer.symbolTable.variables.LocalVariable;
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

    private BlockNode block() throws AbstractSyntacticException, LexicalException {
        match("LlaveAbre");

        Block block = new Block(symbolTable.getCurrentMethod());
        if (!symbolTable.getCurrentMethod().isBlockListEmpty())
            block.setParentBlock(symbolTable.getLastAddedBlock());

        symbolTable.addBlockToCurrentMethod(block);

        BlockNode blockNode= new BlockNode(block);
        block.setCorrespondingBlockNode(blockNode);

        sentenceList(block);
        match("LlaveCierra");

        return blockNode;
    }

    private void sentenceList(Block block) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("SentenceList", currentToken.getTokenName())) {
            List<SentenceNode> sentenceNodes = sentence();
            for(SentenceNode sentenceNode : sentenceNodes) {
                block.addSentenceNode(sentenceNode);
            }
            sentenceList(block);
        } else if (nextsMap.containsEntry("SentenceList", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("SentenceList"));
        }
    }

    private List<SentenceNode> sentence() throws AbstractSyntacticException, LexicalException {
        List<SentenceNode> statementReturned = new ArrayList<>();
        if (firstsMap.containsEntry("Sentence", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("PuntoYComa")) {
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("Block", currentToken.getTokenName())) {
                statementReturned.add(block());
            } else if (firstsMap.containsEntry("AssignmentOrCall", currentToken.getTokenName())) {
                statementReturned.add(assignmentOrCallOrLocalClassicVar());
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("LocalVar", currentToken.getTokenName())) {
                statementReturned = localVar();
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
        return statementReturned;
    }

    private SentenceNode assignmentOrCallOrLocalClassicVar() throws AbstractSyntacticException, LexicalException {

        ExpressionNode expressionNode = expression();
        SentenceNode sentenceNode;
        if (expressionNode.hasRightSide())
            sentenceNode = new AssignmentNode((AssignmentExpressionNode) expressionNode);
        else
            sentenceNode = new CallNode(expressionNode);

        return sentenceNode;
    }

    private ExpressionNode expression() throws AbstractSyntacticException, LexicalException {
        ExpressionNode expressionNode;
        ComposedExpressionNode leftSideComposedExpressionNode = composedExpression();
        AssignmentExpressionNode assignmentExpressionNode = assignmentOrEndOfExpression(leftSideComposedExpressionNode);
        if (assignmentExpressionNode == null)
            expressionNode = new ExpressionNode(leftSideComposedExpressionNode);
        else
            expressionNode = assignmentExpressionNode;

        return expressionNode;
    }

    private ComposedExpressionNode composedExpression() throws AbstractSyntacticException, LexicalException {
        ComposedExpressionNode composedExpressionNode = basicExpression();
        return continueComposedExpression(composedExpressionNode);
    }

    private ComposedExpressionNode basicExpression() throws AbstractSyntacticException, LexicalException {
        ComposedExpressionNode composedExpressionNode;
        if (firstsMap.containsEntry("BasicExpression", currentToken.getTokenName())) {
            if (firstsMap.containsEntry("UnaryOperator", currentToken.getTokenName())) {
                UnaryExpressionNode unaryExpressionNode = unaryOperator();
                OperandNode operand = operand();
                unaryExpressionNode.setOperandNode(operand);
                composedExpressionNode = unaryExpressionNode;
            } else {
                composedExpressionNode = operand();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("BasicExpression"));
        }
        return composedExpressionNode;
    }

    private UnaryExpressionNode unaryOperator() throws AbstractSyntacticException, LexicalException {
        UnaryExpressionNode unaryExpressionNode = null;
        switch (currentToken.getTokenName()) {
            case "Mas" -> {
                unaryExpressionNode = new PlusNode(currentToken);
                match("Mas");
            }
            case "Menos" -> {
                unaryExpressionNode = new MinusNode(currentToken);
                match("Menos");
            }
            case "Not" -> {
                unaryExpressionNode = new NotNode(currentToken);
                match("Not");
            }
            default -> throw new SyntacticException(currentToken, firstsMap.getValue("UnaryOperator")); // not reachable
        }
        return unaryExpressionNode;
    }

    private OperandNode operand() throws AbstractSyntacticException, LexicalException {
        OperandNode operandNode;
        if (firstsMap.containsEntry("Operand", currentToken.getTokenName())) {
            if (firstsMap.containsEntry("Literal", currentToken.getTokenName())) {
                operandNode = literal();
            } else {
                operandNode = access();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("Operand"));
        }
        return operandNode;
    }

    private LiteralNode literal() throws AbstractSyntacticException, LexicalException {
        LiteralNode literalNode;
        if (firstsMap.containsEntry("Literal", currentToken.getTokenName())) {
            if (firstsMap.containsEntry("PrimitiveLiteral", currentToken.getTokenName())) {
                literalNode = primitiveLiteral();
            } else {
                literalNode = objectLiteral();
            }
        } else {
            throw new SyntacticException(currentToken,firstsMap.getValue("Literal"));
        }
        return literalNode;
    }

    private PrimitiveLiteralNode primitiveLiteral() throws AbstractSyntacticException, LexicalException {
        PrimitiveLiteralNode primitiveLiteralNode = new PrimitiveLiteralNode(currentToken);
        switch (currentToken.getTokenName()) {
            case "pr_true" -> match("pr_true");
            case "pr_false" -> match("pr_false");
            case "intLiteral" -> match("intLiteral");
            case "charLiteral" -> match("charLiteral");
            default ->
                    throw new SyntacticException(currentToken, firstsMap.getValue("PrimitiveLiteral"));
        }
        return primitiveLiteralNode;
    }

    private ObjectLiteralNode objectLiteral() throws AbstractSyntacticException, LexicalException {
        ObjectLiteralNode objectLiteralNode = new ObjectLiteralNode(currentToken);
        switch (currentToken.getTokenName()) {
            case "pr_null" -> match("pr_null");
            case "stringLiteral" -> match("stringLiteral");
            default ->
                    throw new SyntacticException(currentToken, firstsMap.getValue("ObjectLiteral")); // not reachable
        }
        return objectLiteralNode;
    }

    private AccessNode access() throws AbstractSyntacticException, LexicalException {
        AccessNode accessNode = new AccessNode();
        PrimaryNode primaryNode = primary();
        accessNode.setPrimaryNode(primaryNode);
        ChainNode chainNode = optionalChain();
        accessNode.setChainNode(chainNode);

        return accessNode;
    }

    private PrimaryNode primary() throws AbstractSyntacticException, LexicalException {
        PrimaryNode primaryNode;
        if (currentToken.getTokenName().equals("idMetVar")) {
            Token idMetVar = currentToken;
            match("idMetVar");
            primaryNode = varOrMethodAccess(idMetVar);
        } else if (firstsMap.containsEntry("ThisAccess", currentToken.getTokenName())) {
            primaryNode = thisAccess();
        } else if (firstsMap.containsEntry("ConstructorAccess", currentToken.getTokenName())) {
            primaryNode = constructorAccess();
        } else if (firstsMap.containsEntry("StaticMethodAccess", currentToken.getTokenName())) {
            primaryNode = staticMethodAccess();
        } else if (firstsMap.containsEntry("ParenthesizedExpression", currentToken.getTokenName())) {
            primaryNode = parenthesizedExpression();
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("Primary"));
        }
        return primaryNode;
    }

    private PrimaryNode varOrMethodAccess(Token idMetVar) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("VarOrMethodAccess", currentToken.getTokenName())) {
            List<ExpressionNode> actualArguments = actualArguments();
            MethodAccessNode methodAccessNode = new MethodAccessNode(idMetVar);
            methodAccessNode.setActualArguments(actualArguments);
            return methodAccessNode;
        } else if (nextsMap.containsEntry("VarOrMethodAccess", currentToken.getTokenName())) {
            // empty. Token is in next list.
            VarAccessNode varAccessNode = new VarAccessNode(idMetVar);
            varAccessNode.setAccessBlock(symbolTable.getLastAddedBlock());

            return varAccessNode;
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("VarOrMethodAccess"));
        }
    }

    private List<ExpressionNode> actualArguments() throws AbstractSyntacticException, LexicalException {
        match("ParentesisAbre");
        List<ExpressionNode> actualArgumentsList = new ArrayList<>();
        optionalExpressionList(actualArgumentsList);
        match("ParentesisCierra");

        return actualArgumentsList;
    }

    private void optionalExpressionList(List<ExpressionNode> actualArgumentsList) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalExpressionList", currentToken.getTokenName())) {
            expressionList(actualArgumentsList);
        } else if (nextsMap.containsEntry("OptionalExpressionList", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalExpressionList"));
        }
    }

    private void expressionList(List<ExpressionNode> actualArgumentsList) throws AbstractSyntacticException, LexicalException {
        actualArgumentsList.add(expression());
        expressionListContinuation(actualArgumentsList);
    }

    private void expressionListContinuation(List<ExpressionNode> actualArgumentsList) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ExpressionListContinuation", currentToken.getTokenName())) {
            match("Coma");
            expressionList(actualArgumentsList);
        } else if (nextsMap.containsEntry("ExpressionListContinuation", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ExpressionListContinuation"));
        }
    }

    private ThisAccessNode thisAccess() throws AbstractSyntacticException, LexicalException {
        ThisAccessNode thisAccessNode = new ThisAccessNode(currentToken);
        thisAccessNode.setThisClass(symbolTable.getCurrentClass());
        match("pr_this");

        return thisAccessNode;
    }

    private ConstructorAccessNode constructorAccess() throws AbstractSyntacticException, LexicalException {
        match("pr_new");
        ConstructorAccessNode constructorAccessNode = new ConstructorAccessNode(currentToken);
        match("idClase");
        optionalGenericConstructorInvocation();
        List<ExpressionNode> actualArguments = actualArguments();
        constructorAccessNode.setActualArguments(actualArguments);

        return constructorAccessNode;
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

    private StaticMethodAccessNode staticMethodAccess() throws AbstractSyntacticException, LexicalException {
        StaticMethodAccessNode staticMethodAccessNode = new StaticMethodAccessNode(currentToken);
        match("idClase");
        match("Punto");
        staticMethodAccessNode.setIdMetVar(currentToken);
        match("idMetVar");
        List<ExpressionNode> actualArguments = actualArguments();
        staticMethodAccessNode.setActualArguments(actualArguments);

        return staticMethodAccessNode;
    }

    private ParenthesizedExpressionNode parenthesizedExpression() throws AbstractSyntacticException, LexicalException {
        match("ParentesisAbre");
        ParenthesizedExpressionNode parenthesizedExpressionNode = new ParenthesizedExpressionNode(expression());
        match("ParentesisCierra");
        return parenthesizedExpressionNode;
    }

    private ChainNode optionalChain() throws AbstractSyntacticException, LexicalException {
        ChainNode chainNode = new ChainNode();
        if (firstsMap.containsEntry("OptionalChain", currentToken.getTokenName())) {
            match("Punto");
            chainNode.setIdMetVar(currentToken);
            match("idMetVar");
            chainNode.setFurtherChainNode(chainedVarOrMethod(chainNode));
        } else if (nextsMap.containsEntry("OptionalChain", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("OptionalChain"));
        }
        return chainNode;
    }

    private ChainNode chainedVarOrMethod(ChainNode chainNode) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ActualArguments", currentToken.getTokenName())) {
            List<ExpressionNode> actualArguments = actualArguments();
            chainNode.setActualArguments(actualArguments);
        }
        return optionalChain();
    }

    private ComposedExpressionNode continueComposedExpression(ComposedExpressionNode leftSideComposedExpressionNode) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueComposedExpression", currentToken.getTokenName())) {
            BinaryExpressionNode binaryExpressionNode = binaryOperator(leftSideComposedExpressionNode);
            ComposedExpressionNode rightSideComposedExpressionNode = basicExpression();
            binaryExpressionNode.setRightSide(rightSideComposedExpressionNode);
            return continueComposedExpression(binaryExpressionNode);
        } else if (nextsMap.containsEntry("ContinueComposedExpression", currentToken.getTokenName())) {
            // empty. Token is in next list.
            return leftSideComposedExpressionNode;
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("ContinueComposedExpression"));
        }
    }

    private BinaryExpressionNode binaryOperator(ComposedExpressionNode leftSideComposedExpressionNode) throws AbstractSyntacticException, LexicalException {
        BinaryExpressionNode binaryExpressionNode = null;
        switch (currentToken.getTokenName()) {
            case "Or" -> {
                binaryExpressionNode = new OrNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("Or");
            }
            case "And" -> {
                binaryExpressionNode = new AndNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("And");
            }
            case "Equals" -> {
                binaryExpressionNode = new EqualsNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("Equals");
            }
            case "Differs" -> {
                binaryExpressionNode = new DiffersNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("Differs");
            }
            case "Mayor" -> {
                binaryExpressionNode = new GreaterNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("Mayor");
            }
            case "MayorIgual" -> {
                binaryExpressionNode = new GreaterOrEqualNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("MayorIgual");
            }
            case "Menor" -> {
                binaryExpressionNode = new LesserNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("Menor");
            }
            case "MenorIgual" -> {
                binaryExpressionNode = new LesserOrEqualNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("MenorIgual");
            }
            case "Mas" -> {
                binaryExpressionNode = new AdditionNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("Mas");
            }
            case "Menos" -> {
                binaryExpressionNode = new SubtractionNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("Menos");
            }
            case "Multiplicador" -> {
                binaryExpressionNode = new MultiplicationNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("Multiplicador");
            }
            case "Divisor" -> {
                binaryExpressionNode = new DivisionNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("Divisor");
            }
            case "Porcentaje" -> {
                binaryExpressionNode = new ModulusNode(currentToken);
                binaryExpressionNode.setLeftSide(leftSideComposedExpressionNode);
                match("Porcentaje");
            }
            default ->
                    throw new SyntacticException(currentToken, firstsMap.getValue("BinaryOperator"));
        }
        return binaryExpressionNode;
    }

    private List<SentenceNode> localVar() throws AbstractSyntacticException, LexicalException {
        List<SentenceNode> localVariableNodes = new ArrayList<>();
        if (firstsMap.containsEntry("LocalVar", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("pr_var")) {
                localVariableNodes.add(localVarVar());
            } else if (firstsMap.containsEntry("Type", currentToken.getTokenName())) {
                Type type = type();
                localVariableNodes = localVarClassic(type);
            }
        } return localVariableNodes;
    }

    private LocalVariableNode localVarVar() throws AbstractSyntacticException, LexicalException {
        match("pr_var");
        LocalVariableNode localVariableNode = new LocalVariableNode(currentToken, symbolTable.getLastAddedBlock());
        LocalVariable localVariable = new LocalVariable(currentToken);
        localVariableNode.setVariable(localVariable);
        // TODO resolver el tipo de la variable local (usar parte derecha).
        match("idMetVar");
        match("Asignacion");
        ComposedExpressionNode rightSideComposedExpressionNode = composedExpression();
        localVariableNode.setRightSide(rightSideComposedExpressionNode);

        return localVariableNode;
    }

    private List<SentenceNode> localVarClassic(Type type) throws AbstractSyntacticException, LexicalException {
        Token currentTokenReference = currentToken;
        List<SentenceNode> localVariables = new ArrayList<>();
        match("idMetVar");

        LocalVariableNode localVariableNode = new LocalVariableNode(currentTokenReference, symbolTable.getLastAddedBlock());
        LocalVariable localVariable = new LocalVariable(currentTokenReference, type);
        localVariableNode.setVariable(localVariable);
        optionalClassicVarInitialization();
        continueLocalVarDeclaration(type);

        return localVariables;
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

    private void continueLocalVarDeclaration(Type type) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueLocalVarDeclaration", currentToken.getTokenName())) {
            match("Coma");
            localVarClassic(type);
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

    private AssignmentExpressionNode assignmentOrEndOfExpression(ComposedExpressionNode composedExpressionNode) throws AbstractSyntacticException, LexicalException {
        AssignmentExpressionNode assignmentExpressionNode = null;
        if (firstsMap.containsEntry("AssignmentOrEndOfExpression", currentToken.getTokenName())) {
            assignmentExpressionNode = assignOperator(composedExpressionNode);
            ComposedExpressionNode rightSideComposedExpressionNode = composedExpression();
            assignmentExpressionNode.setRightSideExpressionNode(rightSideComposedExpressionNode);
        } else if (nextsMap.containsEntry("AssignmentOrEndOfExpression", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("AssignmentOrEndOfExpression"));
        }
        return assignmentExpressionNode;
    }

    private AssignmentExpressionNode assignOperator(ComposedExpressionNode leftSideComposedExpressionNode) throws AbstractSyntacticException, LexicalException {
        AssignmentExpressionNode assignmentExpressionNode;
        if (firstsMap.containsEntry("AssignOperator", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("Asignacion")) {
                match("Asignacion");
                assignmentExpressionNode = new AssignmentExpressionNode(leftSideComposedExpressionNode);
            } else if (currentToken.getTokenName().equals("AsignacionSuma")) {
                match("AsignacionSuma");
                assignmentExpressionNode = new AdditionAssignmentExpressionNode(leftSideComposedExpressionNode);
            } else {
                match("AsignacionResta");
                assignmentExpressionNode = new SubtractionAssignmentExpressionNode(leftSideComposedExpressionNode);
            }
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("AssignOperator"));
        }
        return assignmentExpressionNode;
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
