package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.DuplicateClassException;
import semanticAnalyzer.exceptions.SemanticException;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Class> classTable;
    private Class currentClass;
    private Method currentMethod;

    public SymbolTable() throws SemanticException {
        classTable = new HashMap<>();
        currentClass = null;
        currentMethod = null;
        addPredefinedClassesToTable();
    }

    private void addPredefinedClassesToTable() {
        classTable.put("Object", PredefinedClassCreator.getObjectClass());
        classTable.put("String", PredefinedClassCreator.getStringClass());
        classTable.put("System", PredefinedClassCreator.getSystemClass());
    }

    public void addClass(Class class_) throws DuplicateClassException {
        if (!classTable.containsKey(class_.getName())) {
            classTable.put(class_.getName(), class_);
            currentClass = class_;
        } else
            throw new DuplicateClassException(class_);
    }


    public void addMethodToCurrentClass(Method method) throws SemanticException {
        currentClass.addMethod(method);
        currentMethod = method;
    }

    public void addAttributeToCurrentClass(Attribute attribute) throws SemanticException {
        currentClass.addAttribute(attribute);
    }

    public void addParameterToCurrentMethod(Parameter parameter) throws SemanticException {
        currentClass.addParameterToCurrentMethod(parameter);
    }

    public void addInheritanceToCurrentClass(Token inheritFrom) throws SemanticException {
        currentClass.addInheritance(inheritFrom);
    }

    public Class getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(Class class_) {
        currentClass = class_;
    }

    public Method getCurrentMethod() {
        return currentMethod;
    }

    public boolean thereIsCurrentClass() {
        return currentClass != null;
    }

    public boolean thereIsCurrentMethod() {
        return currentMethod != null;
    }
}
