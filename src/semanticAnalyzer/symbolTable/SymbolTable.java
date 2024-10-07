package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.exceptions.*;

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
        int mainCount = 0;
        for(Class class_ : classTable.values()) {
            mainCount += checkClassMethods(mainCount, class_, class_.getMethodCollection());
            checkForConstructor(class_);
            if (!class_.getInheritsFrom().isEmpty())
                formInheritanceList(class_, class_.getInheritsFrom().getFirst());
            else
                class_.addInheritance(PredefinedClassCreator.getObjectClass().getToken());
        }
    }

    private int checkClassMethods(int mainCount, Class class_, Collection<Method> methodList) throws SemanticException {
        int thereIsMainMethod = 0;
        for (Method method : methodList) {
            if (method.getName().equals("main") && thereIsMainMethod == 0 && method.getType().getName().equals("void") && method.getIsStatic() && method.getParameterCollection().isEmpty())
                thereIsMainMethod++;
            else if (method.getName().equals("main") && thereIsMainMethod > 0)
                throw new DuplicateMainException(method);
            else if (method.getName().equals("main"))
                throw new InvalidMainDeclarationException(class_, method);

            for (Parameter parameter : method.getParameterCollection()) {
                if(!parameter.getType().getIsPrimitive() && classTable.containsKey(parameter.getType().getName()))
                    throw new ClassNotDeclaredException(parameter.getType().getToken());
            }
        }

        return thereIsMainMethod;
    }

    private void checkForConstructor(Class class_) throws SemanticException {
        if (!class_.hasConstructor())
            class_.addDefaultConstructor();
    }

    public void consolidate() throws SemanticException {
        // TODO
        /* En esta pasada tambien
se actualizaran las tablas de metodos y las tablas de variables de las clases en base a la relacion
de herencia, proceso que denominamos consolidacion. En particular, en la consolidacion, se deberan
agregar todos los metodos y las variables que la clase hereda de sus ancestros, con excepcion de aquellos
que esta sobre-escribe. */
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
                            throw new InvalidMethodOverrideException(class_, method);
                    } else {
                        class_.addMethod(method);
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
