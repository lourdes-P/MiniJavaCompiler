package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.CircularInheritanceException;
import semanticAnalyzer.exceptions.DuplicateAttributeException;
import semanticAnalyzer.exceptions.DuplicateMethodException;
import semanticAnalyzer.exceptions.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;

public class Class {
    private HashMap<String,Method> methodTable;
    private HashMap<String,Attribute> attributeTable;
    private Method currentMethod;
    private Token token;
    private ArrayList<Token> inheritsFrom;

    public Class(Token token) {
        this.token = token;
        inheritsFrom = new ArrayList<>();
        attributeTable = new HashMap<>();
        methodTable = new HashMap<>();
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

    public void addParameterToCurrentMethod(Parameter parameter) throws SemanticException {
        currentMethod.addParameter(parameter);
    }


    public void addDefaultConstructor() throws SemanticException {
        Method constructor = new Method();
        constructor.setToken(new Token("idMetVar", this.getName(), 0));
        constructor.setContainerClass(this);
        addMethod(constructor);
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

    public void addInheritance(Token class_) throws CircularInheritanceException {
        if (!class_.getTokenName().equals(this.getName()))
            inheritsFrom.add(class_);
        else
            throw new CircularInheritanceException(this);
    }

    public void addListedInheritance(ArrayList<Token> classInheritanceList) throws CircularInheritanceException {
        for (Token class_ : classInheritanceList) {
            addInheritance(class_);
        }
    }

    public ArrayList<Token> getInheritsFrom() {
        return inheritsFrom;
    }

    public boolean overrides(Method superClassMethod) {
        // TODO
        return false;
    }
}
