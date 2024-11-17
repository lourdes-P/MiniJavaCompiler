package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.expressionNodes.ComposedExpressionNode;
import semanticAnalyzer.exceptions.*;
import semanticAnalyzer.exceptions.part1.*;
import semanticAnalyzer.exceptions.part2.InvalidTypeAttributeInitializationException;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Attribute;
import semanticAnalyzer.symbolTable.variables.Parameter;
import utils.LabelFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class SymbolTable {
    private HashMap<String, Class> classTable;
    private List<Map.Entry<Attribute, ComposedExpressionNode>> attributeInitializationsToCheck;
    private Class currentClass, mainClass;
    private Method currentMethod;
    private FileWriter fileWriter;

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
            if (method.getName().equals("main") && thereIsMainMethod == 0 && method.getType().getName().equals("void") && method.getIsStatic() && method.getParameterCollection().isEmpty()) {
                thereIsMainMethod++;
                mainClass = method.getContainerClass();
            } else if (method.getName().equals("main") && thereIsMainMethod > 0)
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
        Class ancestor = null;
        if (!class_.getName().equals("Object")) {
            if (!class_.isConsolidatedMethods()) {
                ancestor = classTable.get(class_.getInheritsFrom().getFirst().getLexeme());
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
                class_.setConsolidatedMethods(true);

                recalculateMethodOffsets(class_, ancestor.getMethodVTOffset());
            }
        }
    }

    private void recalculateMethodOffsets(Class class_, int methodVTOffset) {
        for (Method method : class_.getStrictlySelfDeclaredMethodCollection()) {
            method.setOffset(method.getOffset() + methodVTOffset);
        }   // TODO si empieza desde cero está bien (chequear)
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
                        class_.addInheritedAttribute(clonedAttribute);
                    }
                }
                class_.setConsolidatedAttributes(true);

                recalculateAttributeOffsets(class_, ancestor.getAttributeCIROffset());
            }
        }
    }

    private void recalculateAttributeOffsets(Class class_, int attributeCIROffset) {
        for(Attribute attribute : class_.getStrictlySelfDeclaredAttributeCollection()) {
            attribute.setOffset((attribute.getOffset() + attributeCIROffset) - 1);
        }   // empiezan siempre desde 1 los atributos (por la ref a VT), por lo que debo restarle 1 (chequear)
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
            else
                inheritanceListFromFirstAncestor = classFromInheritanceList_.getInheritsFrom();
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
        if (childClass.getLexeme().equals("void"))
            return false;
        if (!childClass.getLexeme().equals("null")) {
            if (!classTable.containsKey(childClass.getLexeme()))
                throw new ClassNotDeclaredException(childClass);
        } else {
            return true;
        }
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

    public void setFileWriter(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public void write(String ln) throws IOException {
        fileWriter.write(ln);
    }

    public void generateInterCode() throws IOException {
        write(".CODE\n" +
                "PUSH " + LabelFactory.createLabel("met","main",mainClass.getName()) + "\n" +
                "CALL\n" +
                "HALT\n\n");
        writeSimpleHeapInitPrimitive();
        writeSimpleMalloc();

        for (Class class_ : classTable.values()) {
            class_.generateInterCode(this);
        }
    }

    private void writeSimpleHeapInitPrimitive() throws IOException {
        write("simple_heap_init: RET 0   ; inicializacion simplificada del .heap\n\n");
    }

    private void writeSimpleMalloc() throws IOException {
        write("simple_malloc: LOADFP    ; inicializacion unidad\n" +
                "LOADSP\n"+
                "STOREFP    ; finaliza inicializacion del RA\n"+
                "LOADHL     ; hl\n"+
                "DUP        ; hl\n"+
                "PUSH 1\n"+
                "ADD\n"+
                "STORE 4    ; guarda resultado (puntero a base de bloque)\n"+
                "LOAD 3     ; carga cantidad de celdas a alojar (parametro)\n"+
                "ADD\n"+
                "STOREHL    ; mueve el heap limit (hl)\n"+
                "STOREFP\n"+
                "RET 1      ; retorna eliminando el parametro\n\n");
    }


    public void checkOffsets() {
        // TODO eliminar este metodo
        for (Class class_ : classTable.values()) {
            System.out.println("Class " + class_.getName());
            for (Attribute attribute : class_.getAttributeCollection())
                System.out.println("    Attribute " + attribute.getName() + " offset: " + attribute.getOffset());

            for (Method method : class_.getMethodCollection()) {
                System.out.println("    Method " + method.getName() + " offset: " + method.getOffset());
                for (Parameter parameter : method.getOrderedParameterList())
                    System.out.println(" Parameter " + parameter.getPositionInMethodParameterList() + " offset: " + parameter.getOffset());
                method.checkOffsets();
            }


        }
    }
}
