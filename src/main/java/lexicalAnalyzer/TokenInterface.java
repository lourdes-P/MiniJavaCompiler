package lexicalAnalyzer;

public interface TokenInterface {

    String getTokenName();

    String getLexeme();

    int getLineNumber();

    int getColumnNumber();
}
