package lexicalAnalyzer.reservedWordManager;

import utils.CSVFileManager;

import java.util.HashMap;
import java.util.Map;

public class ReservedWordMap {
    private Map<String,String> reservedWordMap;
    private CSVFileManager CSVFileManager;

    public ReservedWordMap() {
        CSVFileManager = new CSVFileManager("reservedwords.csv");
        reservedWordMap = new HashMap<String, String>();
        initializeMap();
    }

    private void initializeMap() {
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
        for (Object o : array) {
            System.out.println(o);
        }
    }
}
