package utils;

import java.io.InputStream;
import java.util.Scanner;

public class CSVFileManager {
    private Scanner scanner;
    private String fileName;

    public CSVFileManager(String fileName) {
        this.fileName = fileName;
        initializeScanner();
    }

    private void initializeScanner() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        scanner = new Scanner(inputStream);
    }

    public String[] getNextCSVToken() {
        String[] nextToken = null;
        String nextLine;
        if (scanner.hasNextLine()) {
            nextLine = scanner.nextLine();
            nextToken = nextLine.split(",");
        }
        return nextToken;
    }

    public void openNewFile(String filePath) {
        this.close();
        this.fileName = filePath;
        initializeScanner();
    }

    public void close() {
        scanner.close();
    }

}
