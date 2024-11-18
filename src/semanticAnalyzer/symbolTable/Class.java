package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
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
    private HashMap<String, Attribute> attributeTable, strictlySelfDeclaredAttributeTable, invisibleAttributes;
    private Method currentMethod;
    private Token token;
    private ArrayList<Token> inheritsFrom;
    private boolean consolidatedAttributes, consolidatedMethods, defaultConstructor, attributeOffsetsReady;
    private int attributeCIROffset, methodVTOffset;

    public Class(Token token) {
        this.token = token;
        inheritsFrom = new ArrayList<>();
        attributeTable = new HashMap<>();
        strictlySelfDeclaredAttributeTable = new HashMap<>();
        invisibleAttributes = new HashMap<>();
        methodTable = new HashMap<>();
        strictlySelfDeclaredMethodTable = new HashMap<>();
        constructorTable = new HashMap<>();
        consolidatedAttributes = false;
        consolidatedMethods = false;
        attributeCIROffset = 1;         // dejo el primero para la referencia a la VT
        methodVTOffset = 0;
        defaultConstructor = false;
        attributeOffsetsReady = false;
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
            if (!attribute.isStatic())
                attribute.setOffset(attributeCIROffset++);
            attributeTable.put(attribute.getName(), attribute);
            strictlySelfDeclaredAttributeTable.put(attribute.getName(), attribute);
        } else
            throw new DuplicateAttributeException(this, attribute);
    }

    public void addInheritedAttribute(Attribute inheritedAttribute) {
        attributeTable.put(inheritedAttribute.getName(), inheritedAttribute);
    }

    public void addInvisibleAttribute(Attribute attribute) {
        invisibleAttributes.put(attribute.getName(), attribute);
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
        defaultConstructor = true;
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

    public Method getStrictlySelfDeclaredMethod(String methodName) {
        return strictlySelfDeclaredMethodTable.get(methodName);
    }

    public Collection<Method> getStrictlySelfDeclaredMethodCollection() {
        return strictlySelfDeclaredMethodTable.values();
    }

    public Collection<Attribute> getStrictlySelfDeclaredAttributeCollection() {
        return strictlySelfDeclaredAttributeTable.values();
    }

    public Collection<Attribute> getInvisibleAttributeCollection() {
        return invisibleAttributes.values();
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
        Method[] orderedMethodArray = new Method[methodTable.size()];
        int count = 0;
        for (Method method : methodList) {
            orderedMethodArray[method.getOffset()] = method;
        }
        methodList = new ArrayList<>();
        for (int i = 0 ; i < orderedMethodArray.length ; i++) {
            if (orderedMethodArray[i] != null) {
                orderedMethodArray[i].setOffset(count++);
                methodList.add(orderedMethodArray[i]);
            }
        }

        return methodList;
    }

    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        List<Method> nonStaticMethods = new ArrayList<>(), staticMethods = new ArrayList<>();
        List<Attribute> staticAttributes = getStaticAttributes();
        for (Method method : methodTable.values()) {
            if (method.getIsStatic())
                staticMethods.add(method);
            else
                nonStaticMethods.add(method);
        }
        staticMethods.addAll(constructorTable.values());

        symbolTable.write(".DATA\n");
        if (nonStaticMethods.isEmpty()) {
            symbolTable.write(LabelFactory.createVTLabel("VT", getName()) + ": NOP\n");
        } else {
            symbolTable.write(LabelFactory.createVTLabel("VT", getName()) + ": DW " + generateVT(getOrderedMethodList(nonStaticMethods)) + "\n");
        }
        if (!staticAttributes.isEmpty()) {
            symbolTable.write(generateStaticAttributeCode(staticAttributes));
            symbolTable.write("\n.CODE\n");
//            initializeStaticAttributes(symbolTable);
        } else {
            symbolTable.write(".CODE\n");
        }

        this.getConstructor(this.getName()).generateInterCode(symbolTable);
        symbolTable.write("\n");

        for (Method method : getStrictlySelfDeclaredMethodCollection()) {
            method.generateInterCode(symbolTable);
            symbolTable.write("\n");
        }
        symbolTable.write("\n");
    }

    private String generateVT(List<Method> nonStaticMethods) {
        String vtInit = "";
        if (!nonStaticMethods.isEmpty())
            vtInit += LabelFactory.createLabel("met", nonStaticMethods.get(0).getName(), nonStaticMethods.get(0).getContainerClass().getName());
        for (int i = 1; i < nonStaticMethods.size() ; i++) {
            vtInit += ", ";
            vtInit += LabelFactory.createLabel("met", nonStaticMethods.get(i).getName(), nonStaticMethods.get(i).getContainerClass().getName());
        }
        vtInit += "\n";
        return vtInit;
    }

    private String generateStaticAttributeCode(List<Attribute> staticAttributeList) {
        String staticAttributeCode = "";
        for (Attribute attribute : staticAttributeList) {
            staticAttributeCode += LabelFactory.createLabel("attr", attribute.getName(), attribute.getContainerClass().getName()) + ": DW 0\n";
        }

        return staticAttributeCode;
    }

    private void initializeStaticAttributes(SymbolTable symbolTable) throws IOException {
        List<Map.Entry<Attribute, ComposedExpressionNode>> attributesToInit = symbolTable.getAttributeInitializationsToGenerate();
        Attribute attribute;
        ComposedExpressionNode composedExpressionNode;
        int count = 0;

        symbolTable.write("LOADFP ; apila el valor del registro fp\n" +
                "LOADSP ; apila el valor del registro sp\n" +
                "STOREFP ; almacena el tope de la pila en el registro fp\n");
        for (Map.Entry<Attribute, ComposedExpressionNode> attributeEntryInitToGenerate : attributesToInit) {
            attribute = attributeEntryInitToGenerate.getKey();
            composedExpressionNode = attributeEntryInitToGenerate.getValue();
            if (attribute.isStatic() && attribute.getContainerClass().getName().equals(this.getName())) {
                composedExpressionNode.generateInterCode(symbolTable);
                symbolTable.write("PUSH " + LabelFactory.createLabel("attr", attribute.getName(), attribute.getContainerClass().getName()) + "\n");
                symbolTable.write("""
                            SWAP
                            STOREREF 0 ; guardo en atributo estatico
                            """);
                count++;
            }
        }

        symbolTable.write("STOREFP ; almacena el tope de la pila en el registro\n");
        symbolTable.write("RET " + count + "\n");
    }

    public void refactorMethodParameters() {
        for (Method method : getStrictlySelfDeclaredMethodCollection()) {
            method.refactorParameterOffsets();
        }
    }

    private List<Attribute> getStaticAttributes() {
        // not object, string, system
        List<Attribute> staticAttributeList = new ArrayList<>();

        for (Attribute attribute : attributeTable.values()) {
            if (attribute.isStatic()) {
                staticAttributeList.add(attribute);
            }
        }

        return staticAttributeList;
    }

    public List<Attribute> getOrderedDynamicAttributes(Collection<Attribute> attributeCollection) {
        List<Attribute> attributeList = new ArrayList<>();
        for (Attribute attribute : attributeCollection) {
            if (!attribute.isStatic()) {
                attributeList.add(attribute);
            }
        }
        return getOrderedAttributeList(attributeList);
    }

    private List<Attribute> getOrderedAttributeList(List<Attribute> attributeList) {
        Attribute[] orderedAttributeArray = new Attribute[attributeTable.size()];
        for (Attribute attribute : attributeList) {
            orderedAttributeArray[attribute.getOffset()-1] = attribute;
        }
        attributeList = new ArrayList<>();
        for (int i = 0 ; i < orderedAttributeArray.length ; i++) {
            if (orderedAttributeArray[i] != null)
                attributeList.add(orderedAttributeArray[i]);
        }

        attributeOffsetsReady = true;

        return attributeList;
    }

    public boolean hasDefaultConstructor() {
        return defaultConstructor;
    }
}
