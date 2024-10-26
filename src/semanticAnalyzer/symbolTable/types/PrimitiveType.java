package semanticAnalyzer.symbolTable.types;

import lexicalAnalyzer.Token;

public class PrimitiveType extends Type {

    public PrimitiveType(Token token) {
        super(token);
        setIsPrimitive(true);
    }

    public String getType() {
        if(this.getToken().getTokenName().equals("pr_boolean"))
            return "boolean";
        else if(this.getToken().getTokenName().equals("pr_true"))
            return "boolean";
        else if(this.getToken().getTokenName().equals("pr_false"))
            return "boolean";
        else if(this.getToken().getTokenName().equals("intLiteral"))
            return "int";
        else if(this.getToken().getTokenName().equals("pr_int"))
            return "int";
        else if(this.getToken().getTokenName().equals("charLiteral"))
            return "char";
        else if(this.getToken().getTokenName().equals("pr_char"))
            return "char";
        else
            return "";
    }
}
