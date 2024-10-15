package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.*;
import semanticAnalyzer.symbolTable.variables.Attribute;
import semanticAnalyzer.symbolTable.variables.Parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Class {
    private HashMap<String,Constructor> constructorTable;
    private HashMap<String,Method> methodTable;
    private HashMap<String, Attribute> attributeTable;
    private Method currentMethod;
    private Token token;
    private ArrayList<Token> inheritsFrom;
    private boolean consolidatedAttributes, consolidatedMethods;

    public Class(Token token) {
        this.token = token;
        inheritsFrom = new ArrayList<>();
        attributeTable = new HashMap<>();
        methodTable = new HashMap<>();
        constructorTable = new HashMap<>();
        consolidatedAttributes = false;
        consolidatedMethods = false;
    }

    public void addMethod(Method method) throws SemanticException {
        if (!methodTable.containsKey(method.getName())) {
            methodTable.put(method.getName(), method);
            currentMethod = method;
        } else
            throw new DuplicateMethodException(this, method);
    }

    public void addAttribute(Attribute attribute) throws SemanticException  {
        if (!attributeTable.containsKey(attribute.getName())) {
            attributeTable.put(attribute.getName(), attribute);
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
        Constructor constructor = new Constructor(new Token("idMetVar", this.getName(), 0), this);
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


}
