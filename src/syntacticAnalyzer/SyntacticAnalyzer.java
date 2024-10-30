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
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.chainNodes.ChainNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.OperandNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.chainNodes.MethodCallChainNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.chainNodes.VarChainNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.literal.*;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.operandNodes.primary.*;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes.MinusNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes.NotNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes.PlusNode;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.unaryExpressionNodes.UnaryExpressionNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.*;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes.SwitchCaseSentenceNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes.SwitchDefaultSentenceNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.switchSentenceNodes.SwitchSentenceNode;
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
    private Token staticMethodAccessClass;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer, SymbolTable symbolTable) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        firstsMap = new FirstsManager();
        nextsMap = new NextsManager();
        sinErrores = true;
        this.symbolTable = symbolTable;
    }


    private void match(String expectedTokenName) throws AbstractSyntacticException, LexicalException {
        if (expectedTokenName.equals(currentToken.getTokenName())) {
            currentToken = lexicalAnalyzer.nextToken();
        } else
            throw new NoMatchSyntacticException(currentToken, expectedTokenName);
    }

    public void start() throws AbstractSyntacticException, LexicalException, SemanticException {
        currentToken = lexicalAnalyzer.nextToken();
        if (firstsMap.containsEntry("Start", currentToken.getTokenName())) {
            classList();
        } else if (currentToken.getTokenName().equals("EOF")) {
            // end
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("Start"));
        }
    }

    private void classList() throws AbstractSyntacticException, LexicalException, SemanticException {
        if (firstsMap.containsEntry("ClassList", currentToken.getTokenName())) {
            class_();
            classList();
        } else if (nextsMap.containsEntry("ClassList", currentToken.getTokenName())) {
            // empty. Token is in the next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ClassList"));
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
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ClassList"));
        }
    }

    private void optionalGenericClassDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalGenericClassDeclaration", currentToken.getTokenName())) {
            match("Menor");
            match("idClase");
            continueOptionalGenericClassDeclaration();
            match("Mayor");
        } else if (nextsMap.containsEntry("OptionalGenericClassDeclaration", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalGenericClassDeclaration"));
        }
    }

    private void continueOptionalGenericClassDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueOptionalGenericClassDeclaration", currentToken.getTokenName())) {
            match("Coma");
            continueGenericClassDeclaration();
        } else if (nextsMap.containsEntry("ContinueOptionalGenericClassDeclaration", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ContinueOptionalGenericClassDeclaration"));
        }
    }

    private void continueGenericClassDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueGenericClassDeclaration", currentToken.getTokenName())) {
            match("idClase");
            continueOptionalGenericClassDeclaration();
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ContinueGenericClassDeclaration"));
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
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalInheritance"));
        }
    }

    private void memberList() throws AbstractSyntacticException, LexicalException, SemanticException {
        if (firstsMap.containsEntry("MemberList", currentToken.getTokenName())) {
            member();
            memberList();
        } else if (nextsMap.containsEntry("MemberList", currentToken.getTokenName())) {
            // empty. Token is in the next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("MemberList"));
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
            throw new SyntacticException(currentToken, firstsMap.getValue("Member"));
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
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalFormalArgumentList"));
        }
    }

    private void formalArgumentList(ArrayList<Parameter> parameterList) throws AbstractSyntacticException, LexicalException {
        Parameter parameter = formalArgument();
        parameterList.add(parameter);
        parameter.setPositionInMethodParameterList(parameterList.size() - 1);
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
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("StopOrContinueFAL"));
        }
    }

    private Type memberType() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("MemberType", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("pr_void")) {
                Token voidToken = currentToken;
                match("pr_void");
                return new PrimitiveType(voidToken);
            } else {
                return type();
            }
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("MemberType"));
        }
    }

    private Type type() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("Type", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("idClase")) {
                Type type = new ReferenceType(currentToken);
                match("idClase");
                optionalGenericDeclaration();
                return type;
            } else {
                return primitiveType();
            }
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("Type"));
        }

    }

    private void optionalGenericDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalGenericDeclaration", currentToken.getTokenName())) {
            match("Menor");
            match("idClase");
            continueOptionalGenericDeclaration();
            match("Mayor");
        } else if (nextsMap.containsEntry("OptionalGenericDeclaration", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalGenericDeclaration"));
        }
    }

    private void continueOptionalGenericDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueOptionalGenericDeclaration", currentToken.getTokenName())) {
            match("Coma");
            continueGenericDeclaration();
        } else if (nextsMap.containsEntry("ContinueOptionalGenericDeclaration", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ContinueOptionalGenericDeclaration"));
        }
    }

    private void continueGenericDeclaration() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueGenericDeclaration", currentToken.getTokenName())) {
            match("idClase");
            continueOptionalGenericDeclaration();
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ContinueGenericDeclaration"));
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
            throw new SyntacticException(currentToken, firstsMap.getValue("PrimitiveType"));
        }
    }

    private boolean optionalStatic() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalStatic", currentToken.getTokenName())) {
            match("pr_static");
            return true;
        } else if (nextsMap.containsEntry("OptionalStatic", currentToken.getTokenName())) {
            // empty. Token is in next list.
            return false;
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalStatic"));
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
            throw new SyntacticException(currentToken, firstsMap.getValue("AttributeMethod"));
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
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalAttributeInitialization"));
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
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ContinueAttributeDeclaration"));
        }
    }

    private BlockNode block() throws AbstractSyntacticException, LexicalException {
        match("LlaveAbre");

        Block block = new Block(symbolTable.getCurrentMethod());
        if (!symbolTable.getCurrentMethod().isBlockListEmpty())
            block.setParentBlock(symbolTable.getCurrentBlock());

        symbolTable.addBlockToCurrentMethod(block);
        symbolTable.setCurrentBlock(block);

        BlockNode blockNode = new BlockNode(block);
        block.setCorrespondingBlockNode(blockNode);

        sentenceList(block);
        match("LlaveCierra");

        if (block.getParentBlock() != null) {
            symbolTable.setCurrentBlock(block.getParentBlock());
        }

        return blockNode;
    }

    private void sentenceList(Block block) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("SentenceList", currentToken.getTokenName())) {
            List<SentenceNode> sentenceNodes = sentence();
            for (SentenceNode sentenceNode : sentenceNodes) {
                block.addSentenceNode(sentenceNode);
            }
            sentenceList(block);
        } else if (nextsMap.containsEntry("SentenceList", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("SentenceList"));
        }
    }

    private List<SentenceNode> sentence() throws AbstractSyntacticException, LexicalException {
        List<SentenceNode> statementReturned = new ArrayList<>();
        if (firstsMap.containsEntry("Sentence", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("PuntoYComa")) {
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("Block", currentToken.getTokenName())) {
                statementReturned.add(block());
            } else if (currentToken.getTokenName().equals("idClase")) {
                statementReturned = staticMethodAccessOrLocalVarClassic();
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("AssignmentOrCall", currentToken.getTokenName())) {
                statementReturned.add(assignmentOrCall());
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("LocalVar", currentToken.getTokenName())) {
                statementReturned = localVar();
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("Return", currentToken.getTokenName())) {
                statementReturned.add(return_());
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("Break", currentToken.getTokenName())) {
                statementReturned.add(break_());
                match("PuntoYComa");
            } else if (firstsMap.containsEntry("If", currentToken.getTokenName())) {
                statementReturned.add(if_());
            } else if (firstsMap.containsEntry("While", currentToken.getTokenName())) {
                statementReturned.add(while_());
            } else if (firstsMap.containsEntry("Switch", currentToken.getTokenName())) {
                statementReturned.add(switch_());
            }
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("Sentence"));
        }
        return statementReturned;
    }

    private SentenceNode assignmentOrCall() throws AbstractSyntacticException, LexicalException {
        Token token = currentToken;
        ExpressionNode expressionNode = expression();
        SentenceNode sentenceNode;
        if (expressionNode.hasRightSide())
            sentenceNode = new AssignmentNode((AssignmentExpressionNode) expressionNode);
        else
            sentenceNode = new CallNode(token, expressionNode);

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
            throw new SyntacticException(currentToken, firstsMap.getValue("BasicExpression"));
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
            throw new SyntacticException(currentToken, firstsMap.getValue("Operand"));
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
            throw new SyntacticException(currentToken, firstsMap.getValue("Literal"));
        }
        return literalNode;
    }

    private PrimitiveLiteralNode primitiveLiteral() throws AbstractSyntacticException, LexicalException {
        PrimitiveLiteralNode primitiveLiteralNode = null;
        switch (currentToken.getTokenName()) {
            case "pr_true" -> {
                primitiveLiteralNode = new BooleanLiteralNode(currentToken);
                match("pr_true");
            }
            case "pr_false" -> {
                primitiveLiteralNode = new BooleanLiteralNode(currentToken);
                match("pr_false");
            }
            case "intLiteral" -> {
                primitiveLiteralNode = new IntLiteralNode(currentToken);
                match("intLiteral");
            }
            case "charLiteral" -> {
                primitiveLiteralNode = new CharLiteralNode(currentToken);
                match("charLiteral");
            }
            default -> throw new SyntacticException(currentToken, firstsMap.getValue("PrimitiveLiteral"));
        }
        return primitiveLiteralNode;
    }

    private ObjectLiteralNode objectLiteral() throws AbstractSyntacticException, LexicalException {
        ObjectLiteralNode objectLiteralNode = null;
        switch (currentToken.getTokenName()) {
            case "pr_null" -> {
                objectLiteralNode = new NullLiteralNode(currentToken);
                match("pr_null");
            }
            case "stringLiteral" -> {
                objectLiteralNode = new StringLiteralNode(currentToken);
                match("stringLiteral");
            }
            default -> throw new SyntacticException(currentToken, firstsMap.getValue("ObjectLiteral")); // not reachable
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
            methodAccessNode.setClass(symbolTable.getCurrentClass());
            methodAccessNode.setContainerMethod(symbolTable.getCurrentMethod());
            methodAccessNode.setActualArguments(actualArguments);
            return methodAccessNode;
        } else if (nextsMap.containsEntry("VarOrMethodAccess", currentToken.getTokenName())) {
            // empty. Token is in next list.
            VarAccessNode varAccessNode = new VarAccessNode(idMetVar);
            varAccessNode.setAccessBlock(symbolTable.getCurrentBlock());

            return varAccessNode;
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("VarOrMethodAccess"));
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
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalExpressionList"));
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
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ExpressionListContinuation"));
        }
    }

    private ThisAccessNode thisAccess() throws AbstractSyntacticException, LexicalException {
        ThisAccessNode thisAccessNode = new ThisAccessNode(currentToken);
        thisAccessNode.setThisClass(symbolTable.getCurrentClass());
        thisAccessNode.setContainerMethod(symbolTable.getCurrentMethod());
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
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalGenericConstructorInvocation"));
        }
    }

    private void optionalDiamondNotation() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalDiamondNotation", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("Mayor")) {
                match("Mayor");
            } else {
                match("idClase");
                continueOptionalGenericConstructorInvocation();

                match("Mayor");
            }
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("OptionalDiamondNotation"));
        }
    }

    private void continueOptionalGenericConstructorInvocation() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueOptionalGenericConstructorInvocation", currentToken.getTokenName())) {
            match("Coma");
            continueGenericConstructorInvocation();
        } else if (nextsMap.containsEntry("ContinueOptionalGenericConstructorInvocation", currentToken.getTokenName())) {
            // empty.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ContinueOptionalGenericConstructorInvocation"));
        }
    }

    private void continueGenericConstructorInvocation() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueGenericConstructorInvocation", currentToken.getTokenName())) {
            match("idClase");
            continueOptionalGenericConstructorInvocation();
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("ContinueGenericConstructorInvocation"));
        }
    }

    private StaticMethodAccessNode staticMethodAccess() throws AbstractSyntacticException, LexicalException {
        StaticMethodAccessNode staticMethodAccessNode;
        if(currentToken.getTokenName().equals("Punto")) {
            staticMethodAccessNode = new StaticMethodAccessNode(staticMethodAccessClass);

        } else {
            staticMethodAccessNode = new StaticMethodAccessNode(currentToken);
            match("idClase");
        }
        match("Punto");
        staticMethodAccessNode.setIdMetVar(currentToken);
        match("idMetVar");
        List<ExpressionNode> actualArguments = actualArguments();
        staticMethodAccessNode.setActualArguments(actualArguments);
        staticMethodAccessNode.setContainerMethod(symbolTable.getCurrentMethod());

        return staticMethodAccessNode;
    }

    private ParenthesizedExpressionNode parenthesizedExpression() throws AbstractSyntacticException, LexicalException {
        match("ParentesisAbre");
        ParenthesizedExpressionNode parenthesizedExpressionNode = new ParenthesizedExpressionNode(expression());
        match("ParentesisCierra");
        return parenthesizedExpressionNode;
    }

    private ChainNode optionalChain() throws AbstractSyntacticException, LexicalException {
        ChainNode chainNode = null;
        if (firstsMap.containsEntry("OptionalChain", currentToken.getTokenName())) {
            match("Punto");
            Token idMetVar = currentToken;
            match("idMetVar");
            chainNode = chainedVarOrMethod(idMetVar);
        } else if (nextsMap.containsEntry("OptionalChain", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalChain"));
        }
        return chainNode;
    }

    private ChainNode chainedVarOrMethod(Token idMetVar) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ActualArguments", currentToken.getTokenName())) {
            MethodCallChainNode chainNode = new MethodCallChainNode();
            List<ExpressionNode> actualArguments = actualArguments();
            chainNode.setIdMetVar(idMetVar);
            chainNode.setActualArguments(actualArguments);
            chainNode.setFurtherChainNode(optionalChain());
            chainNode.setContainerMethod(symbolTable.getCurrentMethod());
            return chainNode;
        } else {
            VarChainNode chainNode = new VarChainNode();
            chainNode.setIdMetVar(idMetVar);
            chainNode.setFurtherChainNode(optionalChain());
            chainNode.setContainerMethod(symbolTable.getCurrentMethod());
            return chainNode;
        }
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
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ContinueComposedExpression"));
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
            default -> throw new SyntacticException(currentToken, firstsMap.getValue("BinaryOperator"));
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
                localVarClassic(localVariableNodes, type);
            }
        }
        return localVariableNodes;
    }

    private LocalVariableNode localVarVar() throws AbstractSyntacticException, LexicalException {
        match("pr_var");
        LocalVariableNode localVariableNode = new LocalVariableNode(currentToken, symbolTable.getCurrentBlock());
        LocalVariable localVariable = new LocalVariable(currentToken);
        localVariableNode.setVariable(localVariable);
        match("idMetVar");
        match("Asignacion");
        ComposedExpressionNode rightSideComposedExpressionNode = composedExpression();
        localVariableNode.setRightSide(rightSideComposedExpressionNode);
        symbolTable.getCurrentBlock().addLocalVariable(localVariable);

        return localVariableNode;
    }

    private List<SentenceNode> staticMethodAccessOrLocalVarClassic() throws AbstractSyntacticException, LexicalException {
        Type type = type();
        staticMethodAccessClass = type.getToken();
        if (firstsMap.containsEntry("LocalVarClassic", currentToken.getTokenName())) {
            List<SentenceNode> localVariableNodes = new ArrayList<>();
            return localVarClassic(localVariableNodes, type);
        } else if (firstsMap.containsEntry("StaticMethodAccess", currentToken.getTokenName())) {
            return List.of(assignmentOrCall());
        } else {
            List<String> concatenated = firstsMap.getValue("LocalVarClassic");
            concatenated.addAll(firstsMap.getValue("StaticMethodAccess"));
            throw new SyntacticException(currentToken, concatenated);
        }
    }

    private List<SentenceNode> localVarClassic(List<SentenceNode> localVariables, Type type) throws AbstractSyntacticException, LexicalException {
        Token currentTokenReference = currentToken;
        match("idMetVar");

        LocalVariableNode localVariableNode = new LocalVariableNode(currentTokenReference, symbolTable.getCurrentBlock());
        LocalVariable localVariable = new LocalVariable(currentTokenReference, type);
        localVariableNode.setVariable(localVariable);
        localVariables.add(localVariableNode);
        symbolTable.getCurrentBlock().addLocalVariable(localVariable);
        optionalClassicVarInitialization(localVariableNode);
        continueLocalVarDeclaration(localVariables, type);

        return localVariables;
    }

    private void optionalClassicVarInitialization(LocalVariableNode localVariableNode) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("OptionalClassicVarInitialization", currentToken.getTokenName())) {
            match("Asignacion");
            localVariableNode.setRightSide(composedExpression());
        } else if (nextsMap.containsEntry("OptionalClassicVarInitialization", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalClassicVarInitialization"));
        }
    }

    private void continueLocalVarDeclaration(List<SentenceNode> localVariables, Type type) throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("ContinueLocalVarDeclaration", currentToken.getTokenName())) {
            match("Coma");
            localVarClassic(localVariables, type);
        } else if (nextsMap.containsEntry("ContinueLocalVarDeclaration", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("ContinueLocalVarDeclaration"));
        }
    }

    private ReturnNode return_() throws AbstractSyntacticException, LexicalException {
        ReturnNode returnNode = new ReturnNode(currentToken);
        match("pr_return");
        returnNode.setContainerMethod(symbolTable.getCurrentMethod());
        returnNode.setReturnExpression(optionalExpression());
        return returnNode;
    }

    private ExpressionNode optionalExpression() throws AbstractSyntacticException, LexicalException {
        ExpressionNode expressionNode = null;
        if (firstsMap.containsEntry("OptionalExpression", currentToken.getTokenName())) {
            expressionNode = expression();
        } else if (nextsMap.containsEntry("OptionalExpression", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalExpression"));
        }
        return expressionNode;
    }

    private BreakNode break_() throws AbstractSyntacticException, LexicalException {
        BreakNode breakNode = new BreakNode(currentToken);
        match("pr_break");
        breakNode.setContainerBlock(symbolTable.getCurrentBlock());
        return breakNode;
    }

    private IfNode if_() throws AbstractSyntacticException, LexicalException {
        IfNode ifNode = new IfNode(currentToken);
        match("pr_if");
        match("ParentesisAbre");
        ifNode.setCondition(expression());
        match("ParentesisCierra");
        ifNode.setBody(sentence());
        ifNode.setElseBody(else_());

        return ifNode;
    }

    private List<SentenceNode> else_() throws AbstractSyntacticException, LexicalException {
        if (firstsMap.containsEntry("Else", currentToken.getTokenName())) {
            match("pr_else");
            return sentence();
        } else if (nextsMap.containsEntry("Else", currentToken.getTokenName())) {
            // empty. Token is in next list.
            return null;
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("Else"));
        }
    }

    private WhileNode while_() throws AbstractSyntacticException, LexicalException {
        WhileNode whileNode = new WhileNode(currentToken);
        match("pr_while");
        match("ParentesisAbre");
        whileNode.setCondition(expression());
        match("ParentesisCierra");
        whileNode.setWhileSentence(sentence());

        return whileNode;
    }

    private SwitchNode switch_() throws AbstractSyntacticException, LexicalException {
        SwitchNode switchNode = new SwitchNode(currentToken);
        match("pr_switch");
        match("ParentesisAbre");
        switchNode.setCondition(expression());
        match("ParentesisCierra");
        match("LlaveAbre");
        switchSentenceList(switchNode);
        match("LlaveCierra");

        return switchNode;
    }

    private void switchSentenceList(SwitchNode switchNode) throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("SwitchSentenceList", currentToken.getTokenName())) {
            switchNode.addSwitchSentenceToList(switchSentence());
            switchSentenceList(switchNode);
        } else if (nextsMap.containsEntry("SwitchSentenceList", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("SwitchSentenceList"));
        }
    }

    private SwitchSentenceNode switchSentence() throws AbstractSyntacticException, LexicalException {
        if (currentToken.getTokenName().equals("pr_case")) {
            SwitchCaseSentenceNode switchCaseSentenceNode = new SwitchCaseSentenceNode(currentToken);
            match("pr_case");
            switchCaseSentenceNode.setPrimitiveLiteralNode(primitiveLiteral());
            match("DosPuntos");
            switchCaseSentenceNode.setOptionalSentence(optionalSentence());
            return switchCaseSentenceNode;
        } else if (currentToken.getTokenName().equals("pr_default")) {
            SwitchDefaultSentenceNode switchDefaultSentenceNode = new SwitchDefaultSentenceNode(currentToken);
            match("pr_default");
            match("DosPuntos");
            switchDefaultSentenceNode.setSentenceNode(sentence());
            return switchDefaultSentenceNode;
        } else {
            throw new SyntacticException(currentToken, firstsMap.getValue("SwitchSentenceList"));
        }
    }

    private List<SentenceNode> optionalSentence() throws AbstractSyntacticException, LexicalException {
        if(firstsMap.containsEntry("OptionalSentence", currentToken.getTokenName())) {
            return sentence();
        } else if (nextsMap.containsEntry("OptionalSentence", currentToken.getTokenName())) {
            // empty. Token is in next list.
            return null;
        } else {
            throw new SyntacticException(currentToken, concatenateFirstListAndNextList("OptionalSentence"));
        }
    }

    private AssignmentExpressionNode assignmentOrEndOfExpression(ComposedExpressionNode composedExpressionNode) throws AbstractSyntacticException, LexicalException {
        AssignmentExpressionNode assignmentExpressionNode = null;
        if (firstsMap.containsEntry("AssignmentOrEndOfExpression", currentToken.getTokenName())) {
            assignmentExpressionNode = assignOperator(composedExpressionNode);
            ComposedExpressionNode rightSideComposedExpressionNode = composedExpression();
            assignmentExpressionNode.setRightSideComposedExpressionNode(rightSideComposedExpressionNode);
        } else if (nextsMap.containsEntry("AssignmentOrEndOfExpression", currentToken.getTokenName())) {
            // empty. Token is in next list.
        } else {
            throw new SyntacticException(currentToken,concatenateFirstListAndNextList("AssignmentOrEndOfExpression"));
        }
        return assignmentExpressionNode;
    }

    private AssignmentExpressionNode assignOperator(ComposedExpressionNode leftSideComposedExpressionNode) throws AbstractSyntacticException, LexicalException {
        AssignmentExpressionNode assignmentExpressionNode;
        Token assignmentToken;
        if (firstsMap.containsEntry("AssignOperator", currentToken.getTokenName())) {
            if (currentToken.getTokenName().equals("Asignacion")) {
                assignmentToken = currentToken;
                match("Asignacion");
                assignmentExpressionNode = new AssignmentExpressionNode(leftSideComposedExpressionNode);
                assignmentExpressionNode.setAssignmentToken(assignmentToken);
            } else if (currentToken.getTokenName().equals("AsignacionSuma")) {
                assignmentToken = currentToken;
                match("AsignacionSuma");
                assignmentExpressionNode = new AdditionAssignmentExpressionNode(leftSideComposedExpressionNode);
                assignmentExpressionNode.setAssignmentToken(assignmentToken);
            } else {
                assignmentToken = currentToken;
                match("AsignacionResta");
                assignmentExpressionNode = new SubtractionAssignmentExpressionNode(leftSideComposedExpressionNode);
                assignmentExpressionNode.setAssignmentToken(assignmentToken);
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
