package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.BlockNode;
import semanticAnalyzer.exceptions.*;
import semanticAnalyzer.exceptions.part1.CircularInheritanceException;
import semanticAnalyzer.exceptions.part1.DuplicateAttributeException;
import semanticAnalyzer.exceptions.part1.DuplicateConstructorException;
import semanticAnalyzer.exceptions.part1.DuplicateMethodException;
import semanticAnalyzer.symbolTable.variables.Attribute;
import semanticAnalyzer.symbolTable.variables.Parameter;
import utils.LabelFactory;

import java.io.IOException;
import java.util.*;

public class Class {
    private HashMap<String,Constructor> constructorTable;
    private HashMap<String,Method> methodTable, strictlySelfDeclaredMethodTable;
    private HashMap<String, Attribute> attributeTable, strictlySelfDeclaredAttributeTable;
    private Method currentMethod;
    private Token token;
    private ArrayList<Token> inheritsFrom;
    private boolean consolidatedAttributes, consolidatedMethods;
    private int attributeCIROffset, methodVTOffset;

    public Class(Token token) {
        this.token = token;
        inheritsFrom = new ArrayList<>();
        attributeTable = new HashMap<>();
        strictlySelfDeclaredAttributeTable = new HashMap<>();
        methodTable = new HashMap<>();
        strictlySelfDeclaredMethodTable = new HashMap<>();
        constructorTable = new HashMap<>();
        consolidatedAttributes = false;
        consolidatedMethods = false;
        attributeCIROffset = 1;         // dejo el primero para la referencia a la VT
        methodVTOffset = 0;
    }

    public void addMethod(Method method) throws SemanticException {
        if (!methodTable.containsKey(method.getName())) {
            method.setOffset(methodVTOffset++);
            methodTable.put(method.getName(), method);
            strictlySelfDeclaredMethodTable.put(method.getName(), method);
            currentMethod = method;
        } else
            throw new DuplicateMethodException(this, method);
    }

    public void addParentMethod(Method method) throws SemanticException {
        if (!methodTable.containsKey(method.getName())) {
            methodTable.put(method.getName(), method);
        } else
            throw new DuplicateMethodException(this, method);
    }

    public void addAttribute(Attribute attribute) throws SemanticException  {
        if (!attributeTable.containsKey(attribute.getName())) {
            attribute.setOffset(attributeCIROffset++);
            attributeTable.put(attribute.getName(), attribute);
            strictlySelfDeclaredAttributeTable.put(attribute.getName(), attribute);
        } else
            throw new DuplicateAttributeException(this, attribute);
    }

    public void addInvisibleAttribute(Attribute attribute) {
        attributeTable.put(attribute.getName(), attribute);
    }

    public void addParameterToCurrentMethod(Parameter parameter) throws SemanticException {
        currentMethod.addParameter(parameter);
    }

    public void addParameterListToCurrentMethod(List<Parameter> parameterList) throws SemanticException {
        currentMethod.addParameterList(parameterList);
    }

    public void addConstructor(Constructor constructor) throws SemanticException {
        if (constructorTable.isEmpty()) {
            constructorTable.put(constructor.getName(), constructor);
            currentMethod = constructor;
        } else
            throw new DuplicateConstructorException(this, constructor);
    }

    public void addDefaultConstructor() throws SemanticException {
        Constructor constructor = new Constructor(new Token("idClase", this.getName(), 0), this);
        Block block = new Block(constructor);
        constructor.addBlock(block);
        BlockNode blockNode = new BlockNode(block);
        block.setCorrespondingBlockNode(blockNode);

        addConstructor(constructor);
    }

    public void addInheritance(Token class_) throws CircularInheritanceException {
        if (!class_.getLexeme().equals(this.getName()))
            inheritsFrom.add(class_);
        else
            throw new CircularInheritanceException(this);
    }

    public void addListedInheritance(List<Token> classInheritanceList) throws CircularInheritanceException {
        inheritsFrom.clear();
        for (Token class_ : classInheritanceList) {
            addInheritance(class_);
        }
    }

    public int getLineNumber() {
        return token.getLineNumber();
    }

    public String getName() {
        return token.getLexeme();
    }

    public Method getCurrentMethod() {
        return currentMethod;
    }

    public Token getToken() {
        return token;
    }

    public boolean hasConstructor() {
        return !constructorTable.isEmpty();
    }

    public ArrayList<Token> getInheritsFrom() {
        return inheritsFrom;
    }

    public boolean containsInheritance(Token classInheritedFrom) {
        for (Token token : inheritsFrom) {
            if (token.getLexeme().equals(classInheritedFrom.getLexeme()))
                return true;
        }
        return false;
    }

    public Collection<Method> getMethodCollection() {
        return methodTable.values();
    }

    public Collection<Attribute> getAttributeCollection() {
        return attributeTable.values();
    }

    public boolean hasAttribute(Attribute attribute) {
        for (Attribute attributeFromClassList : attributeTable.values()) {
            if (attributeFromClassList.equals(attribute))
                return true;
        }
        return false;
    }

    public boolean hasAttribute(String attributeName) {
        return attributeTable.containsKey(attributeName);
    }

    public boolean overrides(Method superClassMethod) {
        if(methodTable.containsKey(superClassMethod.getName())) {
            return superClassMethod.equals(methodTable.get(superClassMethod.getName()));
        } else
            return false;
    }

    public void setConsolidatedAttributes(boolean consolidatedAttributes) {
        this.consolidatedAttributes = consolidatedAttributes;
    }

    public void setConsolidatedMethods(boolean consolidatedMethods) {
        this.consolidatedMethods = consolidatedMethods;
    }

    public boolean isConsolidatedAttributes() {
        return consolidatedAttributes;
    }

    public boolean isConsolidatedMethods() {
        return consolidatedMethods;
    }

    public boolean hasMethod(String methodName) {
        return methodTable.containsKey(methodName);
    }

    public Method getMethod(String methodName) {
        return methodTable.get(methodName);
    }

    public boolean hasStrictlySelfDeclaredMethod(String methodName) {
        return strictlySelfDeclaredMethodTable.containsKey(methodName);
    }

    public Method getStrictlySelfDeclaredMethod(String methodName) {
        return strictlySelfDeclaredMethodTable.get(methodName);
    }

    public Collection<Method> getStrictlySelfDeclaredMethodCollection() {
        return strictlySelfDeclaredMethodTable.values();
    }

    public Collection<Attribute> getStrictlySelfDeclaredAttributeCollection() {
        return strictlySelfDeclaredAttributeTable.values();
    }

    public Constructor getConstructor(String constructorName) {
        return constructorTable.get(constructorName);
    }

    public Attribute getAttribute(String attributeName) {
        return attributeTable.get(attributeName);
    }

    public int getAttributeCIROffset() {
        return attributeCIROffset;
    }

    public int getMethodVTOffset() {
        return methodVTOffset;
    }

    public List<Method> getOrderedMethodList (List<Method> methodList) {
        Method[] orderedMethodArray = new Method[methodList.size()];
        for (Method method : methodList) {
            orderedMethodArray[method.getOffset()] = method;
        }

        return new ArrayList<>(Arrays.stream(orderedMethodArray).toList());
    }

    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        List<Method> nonStaticMethods = new ArrayList<>(), staticMethods = new ArrayList<>();
        for (Method method : methodTable.values()) {
            if (method.getIsStatic())
                staticMethods.add(method);
            else
                nonStaticMethods.add(method);
        }
        staticMethods.addAll(constructorTable.values());        // TODO ver que hacer con esto

        symbolTable.write(".DATA\n");
        if (nonStaticMethods.isEmpty()) {
            symbolTable.write(LabelFactory.createLabel("VT", getName()) + ": NOP");
        } else {
            symbolTable.write(LabelFactory.createLabel("VT", getName()) + ": DW " + generateVT(getOrderedMethodList(nonStaticMethods)));
        }

        symbolTable.write(".CODE\n");
        this.getConstructor(this.getName()).generateInterCode(symbolTable);

        for (Method method : getStrictlySelfDeclaredMethodCollection()) {
            method.generateInterCode(symbolTable);
        }
    }

    private String generateVT(List<Method> nonStaticMethods) {
        String vtInit = "";
        if (!nonStaticMethods.isEmpty())
            vtInit += LabelFactory.createLabel("met", nonStaticMethods.get(0).getName(), this.getName());
        for (int i = 1; i < nonStaticMethods.size() ; i++) {
            vtInit += ", ";
            vtInit += LabelFactory.createLabel("met", nonStaticMethods.get(i).getName(), this.getName());
        }
        vtInit += "\n";
        return vtInit;
    }
}
