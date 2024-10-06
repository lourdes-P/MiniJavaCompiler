package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.CircularInheritanceException;
import semanticAnalyzer.exceptions.DuplicateClassException;
import semanticAnalyzer.exceptions.InvalidMainDeclarationException;
import semanticAnalyzer.exceptions.SemanticException;

import java.util.*;

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

    public void addParameterListToCurrentMethod(List<Parameter> parameterList) throws SemanticException {
        currentClass.addParameterListToCurrentMethod(parameterList);
    }

    public void addInheritanceToCurrentClass(Token inheritFrom) throws SemanticException {
        currentClass.addInheritance(inheritFrom);
    }

    public void addConstructorToCurrentClass(Constructor constructor) throws SemanticException {
        currentClass.addConstructor(constructor);
    }

    public void checkDeclarations() throws SemanticException {
        for(Map.Entry<String, Class> classEntry : classTable.entrySet()) {
            //TODO
        }
    }

    private int checkClassMethods(Class class_, List<Method> methodList) throws SemanticException {
        int thereIsMainMethod = 0;
        for (Method method : methodList) {
            // TODO chequear que las clases de los parametros de cada metodo existan.
            if (method.getName().equals("main") && method.getType().getName().equals("void") && method.getIsStatic() && method.getParameterCollection().isEmpty())
                thereIsMainMethod++;
            else if (method.getName().equals("main"))
                throw new InvalidMainDeclarationException(class_, method);


        }

        return thereIsMainMethod;
    }

    private void checkForConstructor(Class class_) {
        // TODO si la clase actual no tiene constructor, agregarle uno por defecto
    }

    public void consolidate() {
        // TODO
    }

    public List<Token> formInheritanceList(Class currentClass, Token classFromInheritanceList) throws CircularInheritanceException {
        List<Token> iterationClassList = new ArrayList<>(List.of(classFromInheritanceList));
        List<Token> inheritanceListFromFirstAncestor = new ArrayList<>();
        if (!classTable.get(classFromInheritanceList.getLexeme()).getInheritsFrom().isEmpty()) {
            Class classFromInheritanceList_ = classTable.get(classFromInheritanceList.getLexeme());
            if (!classFromInheritanceList_.getInheritsFrom().contains(PredefinedClassCreator.getObjectClass().getToken()))
                inheritanceListFromFirstAncestor = formInheritanceList(classFromInheritanceList_, classFromInheritanceList_.getInheritsFrom().getFirst());
        }
        iterationClassList.addAll(inheritanceListFromFirstAncestor);

        if (!iterationClassList.contains(PredefinedClassCreator.getObjectClass().getToken()))
            iterationClassList.add(PredefinedClassCreator.getObjectClass().getToken());

        currentClass.addListedInheritance(iterationClassList);

        return iterationClassList;
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
