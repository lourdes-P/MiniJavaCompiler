package utils;

import java.util.HashMap;
import java.util.Map;

public class MapManager {
    private Map<String,String> map;
    private CSVFileManager CSVFileManager;

    public MapManager(String fileName) {
        CSVFileManager = new CSVFileManager(fileName);
        map = new HashMap<String, String>();
        initializeMap();
    }

    private void initializeMap() {
        String[] keyAndValue;
        while ((keyAndValue = CSVFileManager.getNextCSVToken()) != null) {
            map.put(keyAndValue[1], keyAndValue[0]);
        }
    }

    public String getValue(String word) {
        return map.get(word);
    }

    public boolean isKey(String word) {
        return map.get(word) != null;
    }

    public void showMap() {
        Object[] array = map.entrySet().stream().toArray();
        for (Object o : array) {
            System.out.println(o);
        }
    }
}
