package semanticAnalyzer.symbolTable;

import lexicalAnalyzer.Token;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.BlockNode;
import semanticAnalyzer.abstractSyntacticTree.sentenceNodes.SentenceNode;
import semanticAnalyzer.exceptions.SemanticException;
import semanticAnalyzer.exceptions.part2.expressionExceptions.InvalidDynamicAttributeUseException;
import semanticAnalyzer.symbolTable.variables.Attribute;
import semanticAnalyzer.symbolTable.variables.LocalVariable;
import semanticAnalyzer.symbolTable.variables.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Block {
    private Method containerMethod;
    private HashMap<String, LocalVariable> declaredVariablesInBlock;
    private BlockNode correspondingBlockNode;
    private Block parentBlock;
    private List<SentenceNode> sentenceNodeList;


    public Block(Method containerMethod) {
        this.containerMethod = containerMethod;
        declaredVariablesInBlock = new HashMap<>();
        sentenceNodeList = new ArrayList<>();
        parentBlock = null;
        correspondingBlockNode = null;
    }

    public Block(Method containerMethod, Block parentBlock) {
        this.containerMethod = containerMethod;
        this.parentBlock = parentBlock;
        declaredVariablesInBlock = new HashMap<>();
        sentenceNodeList = new ArrayList<>();
        correspondingBlockNode = null;
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
    public void addLocalVariable(LocalVariable localVariable) {
        declaredVariablesInBlock.put(localVariable.getName(), localVariable);
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

    public boolean hasNestedWhileOrSwitchStatement() {
        for(SentenceNode sentenceNode : sentenceNodeList) {
            if (sentenceNode.isWhileOrSwitchStatement())
                return true;
        }

        if (parentBlock != null)
            return parentBlock.hasNestedWhileOrSwitchStatement();
        else
            return false;
    }

    public void statementCheck(SymbolTable symbolTable) throws SemanticException {
        correspondingBlockNode.statementCheck(symbolTable);
    }
}
