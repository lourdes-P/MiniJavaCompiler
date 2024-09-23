package utils;

import java.util.*;

public class MapManager {
    private Map<String,List<String>> map;
    private CSVFileManager CSVFileManager;

    public MapManager(String fileName) {
        CSVFileManager = new CSVFileManager(fileName);
        map = new HashMap<String, List<String>>();
        initializeMap();
    }

    private void initializeMap() {
        String[] keyAndValue;
        List<String> valueList;
        while ((keyAndValue = CSVFileManager.getNextCSVToken()) != null) {
            if(!map.containsKey(keyAndValue[0])) {
                valueList = new ArrayList<>();
                map.put(keyAndValue[0], valueList);
            }
            map.get(keyAndValue[0]).add(keyAndValue[1]);
        }
        // TODO comentar flattenMap() si se quiere que el mapa se muestre anidado.
        flattenMap();
    }

    private void flattenMap() {
        Set<String> keySet = map.keySet();
        List<String> keyList = keySet.stream().toList();
        List<String> valueListCopy;
        for (String key : keyList) {
            valueListCopy = map.get(key).stream().toList();
            for (String value : valueListCopy) {
                if (map.containsKey(value)) {
                    flattenNestedList(key, value);
                }
            }
        }
    }

    private Set<String> flattenNestedList(String key, String value) {
        List<String> valueList = map.get(value).stream().toList();
        Set<String> listReplacement = new HashSet<>();
        for (String valueFromValueList : valueList) {
            if (map.containsKey(valueFromValueList)) {
                listReplacement.addAll(flattenNestedList(value, valueFromValueList));
            } else {
                if (!map.get(key).contains(valueFromValueList))
                    listReplacement.add(valueFromValueList);
            }
        }
        map.get(key).remove(value);
        map.get(key).addAll(listReplacement);
        return listReplacement;
    }

    public List<String> getValue(String key) {
        return map.get(key);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public boolean containsEntry(String productionName, String tokenName) {
        // TODO descomentar el siguiente código comentado y comentar la línea return containsSingleEntry(productionName, tokenName); para mostrar el mapa anidado
/*
        List<String> valueList;
        boolean doesContain = false;
        if(containsSingleEntry(productionName,tokenName)) {
            doesContain = true;
        } else if (map.containsKey(productionName)) {
            valueList = getValue(productionName);
            for (String value : valueList) {
                if (map.containsKey(value)) {
                    doesContain = containsEntry(value, tokenName);
                    if (doesContain)
                        break;
                }
            }
        }
        return doesContain;


 */

        return containsSingleEntry(productionName, tokenName);
    }

    public boolean containsSingleEntry(String key, String value) {
        if(map.containsKey(key))
            return map.get(key).contains(value);
        else
            return false;
    }

    public void showMap() {
        Object[] array = map.entrySet().stream().toArray();
        for (Object o : array) {
            System.out.println(o);
        }
    }
}
