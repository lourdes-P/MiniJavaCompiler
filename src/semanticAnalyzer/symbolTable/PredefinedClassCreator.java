package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.BlockNode;
import semanticAnalyzer.exceptions.part1.CircularInheritanceException;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.symbolTable.types.PrimitiveType;
import semanticAnalyzer.symbolTable.types.ReferenceType;
import semanticAnalyzer.symbolTable.types.Type;
import semanticAnalyzer.symbolTable.variables.Parameter;

public class PredefinedClassCreator {
    private static Class object;
    private static Class system;
    private static Class string;


    public static Class getObjectClass() {
        if (object == null) {
            try {
                object = createObjectClass();
            } catch (SemanticException e) {
                System.out.println("Error while creating Object class.");
            }
        }
        return object;
    }

    public static Class getStringClass() {
        if (string == null) {
            try {
                string = createStringClass();
            } catch (CircularInheritanceException semanticException) {
                System.out.println("Error while creating String class.");
            }
        }
        return string;
    }

    public static Class getSystemClass() {
        if (system == null) {
            try {
                system = createSystemClass();
            } catch (SemanticException e) {
                System.out.println("Error while creating System class.");
            }
        }
        return system;
    }

    private static Class createObjectClass() throws SemanticException{
        Class object = new Class(new Token("idClase", "Object", 0));

        Method debugPrint = new Method(true, new Token("idMetVar", "debugPrint", 0), object, new PrimitiveType(new Token("pr_void", "void", 0)));

        Parameter i = new Parameter(new Token("idMetVar", "i", 0), new PrimitiveType(new Token("pr_int", "int", 0)), debugPrint, 1);
        i.setPositionInMethodParameterList(0);
        debugPrint.addParameter(i);
        Block block = new Block(debugPrint);
        block.setCorrespondingBlockNode(new BlockNode(block));
        debugPrint.addBlock(block);

        object.addMethod(debugPrint);

        object.setConsolidatedAttributes(true);
        object.setConsolidatedMethods(true);

        return object;
    }

    private static Class createStringClass() throws CircularInheritanceException {
        Class string = new Class(new Token("idClase", "String", 0));
        string.addInheritance(PredefinedClassCreator.getObjectClass().getToken());

        return string;
    }

    private static Class createSystemClass() throws SemanticException {
        Class system = new Class(new Token("idClase", "System", 0));

        Method read = new Method(true, new Token("idMetVar", "read", 0), system, new PrimitiveType(new Token("pr_int", "int", 0)));
        setBlock(read);
        system.addMethod(read);

        Parameter b = new Parameter(new Token("idMetVar", "b", 0), new PrimitiveType(new Token("pr_boolean", "boolean", 0)), 0);
        b.setPositionInMethodParameterList(0);
        Method printB = createMethod("printB", system, new PrimitiveType(new Token("pr_void", "void", 0)), b);
        setBlock(printB);
        system.addMethod(printB);

        Parameter c = new Parameter(new Token("idMetVar", "c", 0), new PrimitiveType(new Token("pr_char", "char", 0)), 0);
        c.setPositionInMethodParameterList(0);
        Method printC = createMethod("printC", system, new PrimitiveType(new Token("pr_void", "void", 0)), c);
        setBlock(printC);
        system.addMethod(printC);

        Parameter i = new Parameter(new Token("idMetVar", "i", 0), new PrimitiveType(new Token("pr_int", "int", 0)), 0);
        i.setPositionInMethodParameterList(0);
        Method printI = createMethod("printI", system, new PrimitiveType(new Token("pr_void", "void", 0)), i);
        setBlock(printI);
        system.addMethod(printI);

        Parameter s = new Parameter(new Token("idMetVar", "s", 0), new ReferenceType(new Token("idClase", "String", 0)), 0);
        s.setPositionInMethodParameterList(0);
        Method printS = createMethod("printS", system, new PrimitiveType(new Token("pr_void", "void", 0)), s);
        setBlock(printS);
        system.addMethod(printS);

        Method println = new Method(true, new Token("idMetVar", "println", 0), system, new PrimitiveType(new Token("pr_void", "void", 0))) ;
        setBlock((println));
        system.addMethod(println);

        Parameter bln = new Parameter(new Token("idMetVar", "b", 0), new PrimitiveType(new Token("pr_boolean", "boolean", 0)), 0);
        bln.setPositionInMethodParameterList(0);
        Method printBln = createMethod("printBln", system, new PrimitiveType(new Token("pr_void", "void", 0)), bln);
        setBlock(printBln);
        system.addMethod(printBln);

        Parameter cln = new Parameter(new Token("idMetVar", "c", 0), new PrimitiveType(new Token("pr_char", "char", 0)), 0);
        cln.setPositionInMethodParameterList(0);
        Method printCln = createMethod("printCln", system, new PrimitiveType(new Token("pr_void", "void", 0)), cln);
        setBlock(printCln);
        system.addMethod(printCln);

        Parameter iln = new Parameter(new Token("idMetVar", "i", 0), new PrimitiveType(new Token("pr_int", "int", 0)), 0);
        iln.setPositionInMethodParameterList(0);
        Method printIln = createMethod("printIln", system, new PrimitiveType(new Token("pr_void", "void", 0)), iln);
        setBlock(printIln);
        system.addMethod(printIln);

        Parameter sln = new Parameter(new Token("idMetVar", "s", 0), new ReferenceType(new Token("idClase", "String", 0)), 0);
        sln.setPositionInMethodParameterList(0);
        Method printSln = createMethod("printSln", system, new PrimitiveType(new Token("pr_void", "void", 0)), sln);
        setBlock(printSln);
        system.addMethod(printSln);

        system.addInheritance(PredefinedClassCreator.getObjectClass().getToken());

        return system;
    }

    private static Method createMethod(String name, Class containerClass, Type type, Parameter parameter) throws SemanticException {
        Method method = new Method(true, new Token("idMetVar", name, 0), containerClass, type);
        parameter.setContainerMethod(method);
        method.addParameter(parameter);
        return method;
    }

    private static void setBlock(Method method) {
        Block block = new Block(method);
        block.setCorrespondingBlockNode(new BlockNode(block));
        method.addBlock(block);
    }
}
