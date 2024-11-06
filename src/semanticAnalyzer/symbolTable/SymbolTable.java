package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.exceptions.*;
import semanticAnalyzer.exceptions.part1.*;
import semanticAnalyzer.exceptions.part2.InvalidTypeAttributeInitializationException;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Attribute;
import semanticAnalyzer.symbolTable.variables.Parameter;

import java.util.*;

public class SymbolTable {
    private HashMap<String, Class> classTable;
    private List<Map.Entry<Attribute, ComposedExpressionNode>> attributeInitializationsToCheck;
    private Class currentClass;
    private Method currentMethod;

    public SymbolTable() throws SemanticException {
        classTable = new HashMap<>();
        attributeInitializationsToCheck = new ArrayList<>();
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
        currentMethod = constructor;
    }

    public void addBlockToCurrentMethod(Block block) {
        currentMethod.addBlock(block);
    }

    public Block getLastAddedBlock() {
        return currentMethod.getLastAddedBlock();
    }

    public void checkDeclarations() throws SemanticException {
        int mainCount = 0;
        for(Class class_ : classTable.values()) {
            mainCount += checkClassMethods(mainCount, class_, class_.getMethodCollection());
            checkForConstructor(class_);
            checkAttributes(class_);
            if (!class_.getInheritsFrom().isEmpty())
                formInheritanceList(class_, class_, class_.getInheritsFrom().getFirst());
            else if (!class_.getName().equals("Object"))
                class_.addInheritance(PredefinedClassCreator.getObjectClass().getToken());
        }

        if (mainCount == 0)
            throw new NonexistentMainMethodException();
    }

    private int checkClassMethods(int thereIsMainMethod, Class class_, Collection<Method> methodList) throws SemanticException {
        for (Method method : methodList) {
            if (method.getName().equals("main") && thereIsMainMethod == 0 && method.getType().getName().equals("void") && method.getIsStatic() && method.getParameterCollection().isEmpty())
                thereIsMainMethod++;
            else if (method.getName().equals("main") && thereIsMainMethod > 0)
                throw new DuplicateMainException(method);
            else if (method.getName().equals("main"))
                throw new InvalidMainDeclarationException(class_, method);
            else if (!method.getType().getIsPrimitive() && !classTable.containsKey(method.getType().getName())) {
                throw new ClassNotDeclaredException(method.getType().getToken());
            }

            for (Parameter parameter : method.getParameterCollection()) {
                if(!parameter.getType().getIsPrimitive() && !classTable.containsKey(parameter.getType().getName()))
                    throw new ClassNotDeclaredException(parameter.getType().getToken());
            }
        }

        return thereIsMainMethod;
    }

    private void checkAttributes(Class class_) throws SemanticException {
        for (Attribute attribute : class_.getAttributeCollection()) {
            if (!attribute.getType().getIsPrimitive() && !classTable.containsKey(attribute.getType().getName()))
                throw new ClassNotDeclaredException(attribute.getType().getToken());
        }
    }

    private void checkForConstructor(Class class_) throws SemanticException {
        if (!class_.hasConstructor())
            class_.addDefaultConstructor();
    }

    public void consolidate() throws SemanticException {
        for(Class class_ : classTable.values()) {
            checkAndUpdateMethodTable(class_);
            checkAndUpdateAttributeTable(class_);
        }
    }

    private void checkAndUpdateMethodTable(Class class_) throws SemanticException {
        if (!class_.getName().equals("Object")) {
            if (!class_.isConsolidatedMethods()) {
                Class ancestor = classTable.get(class_.getInheritsFrom().getFirst().getLexeme());
                if (!ancestor.isConsolidatedMethods())
                    checkAndUpdateMethodTable(ancestor);
                for (Method method : ancestor.getMethodCollection()) {
                    if (class_.hasMethod(method.getName())) {
                        if (!class_.overrides(method))
                            throw new InvalidMethodOverrideException(class_, class_.getMethod(method.getName()));
                    } else {
                        class_.addParentMethod(method);
                    }
                }
            }
        }
    }

    private void checkAndUpdateAttributeTable(Class class_) throws SemanticException {
        if (!class_.getName().equals("Object")) {
            if (!class_.isConsolidatedAttributes()) {
                Class ancestor = classTable.get(class_.getInheritsFrom().getFirst().getLexeme());
                if (!ancestor.isConsolidatedAttributes())
                    checkAndUpdateAttributeTable(ancestor);
                for (Attribute attribute : ancestor.getAttributeCollection()) {
                    if (class_.hasAttribute(attribute.getName())) {
                        Attribute clonedAttribute = Attribute.clone(attribute);
                        clonedAttribute.setInvisibleToContainer(true);
                        class_.addInvisibleAttribute(clonedAttribute);
                    } else {
                        Attribute clonedAttribute = Attribute.clone(attribute);
                        class_.addAttribute(clonedAttribute);
                    }
                }
                class_.setConsolidatedAttributes(true);
            }
        }
    }

    public List<Token> formInheritanceList(Class startingClass, Class currentClass, Token classFromInheritanceList) throws CircularInheritanceException, ClassNotDeclaredException {
        List<Token> iterationClassList = new ArrayList<>(List.of(classFromInheritanceList));
        List<Token> inheritanceListFromFirstAncestor = new ArrayList<>();

        if(startingClass.getName().equals(classFromInheritanceList.getLexeme()))
            throw new CircularInheritanceException(startingClass);

        if (classTable.containsKey(classFromInheritanceList.getLexeme()) && !classTable.get(classFromInheritanceList.getLexeme()).getInheritsFrom().isEmpty()) {
            Class classFromInheritanceList_ = classTable.get(classFromInheritanceList.getLexeme());
            if (!classFromInheritanceList_.getInheritsFrom().contains(PredefinedClassCreator.getObjectClass().getToken()))
                inheritanceListFromFirstAncestor = formInheritanceList(startingClass, classFromInheritanceList_, classFromInheritanceList_.getInheritsFrom().getFirst());
        } else if (!classTable.containsKey(classFromInheritanceList.getLexeme()))
            throw new ClassNotDeclaredException(classFromInheritanceList);

        iterationClassList.addAll(inheritanceListFromFirstAncestor);

        if (!iterationClassList.contains(PredefinedClassCreator.getObjectClass().getToken()))
            iterationClassList.add(PredefinedClassCreator.getObjectClass().getToken());

        currentClass.addListedInheritance(iterationClassList);

        return iterationClassList;
    }

    public void statementCheck() throws SemanticException {
        for (Class class_ : classTable.values()) {
            for (Method method : class_.getMethodCollection()) {
                if (method.getContainerClass().getName().equals(class_.getName()))
                    method.statementCheck(this);
            }
            class_.getConstructor(class_.getName()).statementCheck(this);
        }

        for (Map.Entry<Attribute,ComposedExpressionNode> entry : attributeInitializationsToCheck) {
            checkInitializedAttribute(entry.getKey(), entry.getValue());
        }
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

    public Block getCurrentBlock() {
        return currentMethod.getCurrentBlock();
    }

    public void setCurrentBlock(Block block) {
        currentMethod.setCurrentBlock(block);
    }

    public boolean thereIsCurrentClass() {
        return currentClass != null;
    }

    public boolean thereIsCurrentMethod() {
        return currentMethod != null;
    }

    public boolean containsClass(String className) {
        return classTable.containsKey(className);
    }

    public Class getClass(String className) {
        return classTable.get(className);
    }

    public boolean extendsClass(Token childClass, Token parentClass) throws SemanticException {
        if (!classTable.containsKey(childClass.getLexeme()))
            throw new ClassNotDeclaredException(childClass);
        if (!classTable.containsKey(parentClass.getLexeme()))
            throw new ClassNotDeclaredException(parentClass);

        return classTable.get(childClass.getLexeme()).containsInheritance(parentClass);
    }

    public void checkInitializedAttribute(Attribute attribute, ComposedExpressionNode composedExpressionNode) throws SemanticException {
        Type type = composedExpressionNode.statementCheck(this);

        if (attribute.getType().getType().equals(type.getType())) {

        } else if (!attribute.getType().getIsPrimitive() && !type.getIsPrimitive() && !type.getType().equals("null") && extendsClass(new Token("idClase", type.getType(), type.getToken().getLineNumber()), attribute.getType().getToken())) {

        } else if (!type.getType().equals("null")) {
            throw new InvalidTypeAttributeInitializationException(attribute.getToken());
        }
    }

    public void addInitializedAttributeToCheck(Attribute attribute, ComposedExpressionNode composedExpressionNode) {
        attributeInitializationsToCheck.add(new AbstractMap.SimpleEntry<>(attribute, composedExpressionNode));
    }

    public Method getClassSelfDeclaredMethod(String className, String methodName) {
        return classTable.get(className).getStrictlySelfDeclaredMethod(methodName);
    }

    public Constructor getClassConstructor(String className) {
        return classTable.get(className).getConstructor(className);
    }

}
