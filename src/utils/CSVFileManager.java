package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Error al leer .csv de palabras reservadas.");
        }
        if (inputStream != null)
            scanner = new Scanner(inputStream);
        else
            System.out.println("Error al leer .csv de palabras reservadas.");
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
