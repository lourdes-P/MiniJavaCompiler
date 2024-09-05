package lexicalAnalyzer.reservedWordManager;

import utils.CSVFileManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReservedWordMap {
    Map<String,String> reservedWordMap;
    CSVFileManager CSVFileManager;

    public ReservedWordMap() {
        CSVFileManager = new CSVFileManager("reservedwords.csv");
        reservedWordMap = new HashMap<String, String>();
        initializeMap();
    }

    private void initializeMap() {
        String reservedWord, reservedWordID;
        String[] reservedWordAndReservedWordID;
        while ((reservedWordAndReservedWordID = CSVFileManager.getNextCSVToken()) != null) {
            reservedWordMap.put(reservedWordAndReservedWordID[0], reservedWordAndReservedWordID[1]);
        }
    }

    public String getReservedWordID(String word) {
        return reservedWordMap.get(word);
    }

    public boolean isReservedWord(String word) {
        return reservedWordMap.get(word) != null;
    }

    public void showMap() {
        Object[] array = reservedWordMap.entrySet().stream().toArray();
        for (int i=0; i<array.length ; i++) {
            System.out.println(array[i]);
        }
    }
}
