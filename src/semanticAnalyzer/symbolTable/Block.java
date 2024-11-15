package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.BlockNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidDynamicAttributeUseException;
import semanticAnalyzer.exceptions.part2.statementExceptions.DuplicateLocalVariableNameException;
import semanticAnalyzer.exceptions.part2.statementExceptions.IncorrectTypeException;
import semanticAnalyzer.symbolTable.variables.Attribute;
import semanticAnalyzer.symbolTable.variables.LocalVariable;
import semanticAnalyzer.symbolTable.variables.Variable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Block {
    private Method containerMethod;
    private HashMap<String, LocalVariable> declaredVariablesInBlock;
    private BlockNode correspondingBlockNode;
    private Block parentBlock;
    private List<SentenceNode> sentenceNodeList;
    private int localVarRAOffset;
    private boolean whileOrSwitchBlock;


    public Block(Method containerMethod) {
        this.containerMethod = containerMethod;
        declaredVariablesInBlock = new HashMap<>();
        sentenceNodeList = new ArrayList<>();
        parentBlock = null;
        correspondingBlockNode = null;
        whileOrSwitchBlock = false;
        localVarRAOffset = 0;
    }

    public Block(Method containerMethod, Block parentBlock) {
        this.containerMethod = containerMethod;
        this.parentBlock = parentBlock;
        declaredVariablesInBlock = new HashMap<>();
        sentenceNodeList = new ArrayList<>();
        correspondingBlockNode = null;
        whileOrSwitchBlock = false;
        localVarRAOffset = parentBlock.getOffset();
        // TODO chequear que siga bien
    }

    public void setCorrespondingBlockNode(BlockNode correspondingBlockNode) {
        this.correspondingBlockNode = correspondingBlockNode;
    }

    public void setParentBlock(Block parentBlock) {
        this.parentBlock = parentBlock;
    }

    public Block getParentBlock() {
        return parentBlock;
    }

    public void addSentenceNode(SentenceNode sentenceNode) {
        sentenceNodeList.add(sentenceNode);
    }

    public List<SentenceNode> getSentenceNodeList() {
        return sentenceNodeList;
    }

    public void setWhileOrSwitchBlock(boolean whileOrSwitchBlock) {
        this.whileOrSwitchBlock = whileOrSwitchBlock;
    }

    public boolean isWhileOrSwitchBlock() {
        return whileOrSwitchBlock;
    }
    public void addLocalVariable(LocalVariable localVariable) throws SemanticException {
        if (!declaredVariablesInBlock.containsKey(localVariable.getName())) {
            localVariable.setOffset(localVarRAOffset--); // Notese que a raiz de que el .stack crece desde las direcciones altas hacia las
            //bajas, las variables locales a una unidad poseeran desplazamientos no positivos a partir de 0.
            declaredVariablesInBlock.put(localVariable.getName(), localVariable);
        } else {
            throw new DuplicateLocalVariableNameException(localVariable.getToken());
        }
    }

    public HashMap<String, LocalVariable> getDeclaredVariablesInBlock() {
        return declaredVariablesInBlock;
    }
    public boolean isMainBlock() {
        return parentBlock == null;
    }

    public boolean declaredVariableUniqueToContainerMethod(Token localVariable) {
        if (parentsContain(localVariable)) {
            return false;
        } else if (containerMethod.containsParameter(localVariable.getLexeme())) {
            return false;
        } else {
            return declaredVariablesInBlock.containsKey(localVariable.getLexeme());
        }
    }

    public boolean canAccessVariable(Token variable) {
        return parentsContain(variable) || containerMethod.containsParameter(variable.getLexeme()) || declaredVariablesInBlock.containsKey(variable.getLexeme()) || containerMethod.getContainerClass().hasAttribute(variable.getLexeme());
    }

    public Variable getAccessedVariable(Token variable) throws SemanticException {
        if(parentsContain(variable))
            return getParentsVariable(variable);
        else if (containerMethod.containsParameter(variable.getLexeme()))
            return containerMethod.getParameter(variable.getLexeme());
        else if (declaredVariablesInBlock.containsKey(variable.getLexeme()))
            return declaredVariablesInBlock.get(variable.getLexeme());
        else if(containerMethod.getContainerClass().hasAttribute(variable.getLexeme())) {
            Attribute attribute = containerMethod.getContainerClass().getAttribute(variable.getLexeme());
            if (containerMethod.getIsStatic() && !attribute.isStatic())
                throw new InvalidDynamicAttributeUseException(variable);
            return attribute;
        }
        else
            return null;
    }

    private boolean parentsContain(Token localVariable) {
        return getParentsVariable(localVariable) != null;
    }

    public Variable getParentsVariable(Token localVariable) {
        Block currentBlock = parentBlock;
        Variable variable = null;
        boolean doesContain = false;
        while (currentBlock != null && !doesContain) {
            variable = currentBlock.getDeclaredVariablesInBlock().get(localVariable.getLexeme());
            doesContain = variable != null;
            currentBlock = currentBlock.getParentBlock();
        }
        if (variable != null)
            variable = (localVariable.getLineNumber() > variable.getLineNumber()) ? variable : null;
        return variable;
    }

    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        correspondingBlockNode.statementCheck(symbolTable);
    }

    public void setOffset(int offset) {
        this.localVarRAOffset = offset;
    }

    public int getOffset() {
        return localVarRAOffset;
    }

    public void checkOffsets() {
        for (LocalVariable variable : declaredVariablesInBlock.values()) {
            System.out.println("Local variable" + variable.getName() + " offset: " + variable.getOffset());
        }
    }

    public void generateInterCode(SymbolTable symbolTable) throws IOException {
        symbolTable.write("RMEM "+ declaredVariablesInBlock.size() + "\n");

        for (SentenceNode sentenceNode : sentenceNodeList) {
            sentenceNode.generateInterCode(symbolTable);
        }

        symbolTable.write("FMEM "+ declaredVariablesInBlock.size() + "\n");
    }
}
