package com.ryangrillo.warehousemgt.api.util;

import com.ryangrillo.warehousemgt.api.exception.DataInputException;

import java.util.HashMap;
import java.util.Map;

public class BayUtil {

    public static String createNewBayLabel(int currentRow, int currentShelf, int currentLevel, int maxRows, int maxLength, int maxHeight) throws Exception {
        if (currentRow == maxRows && currentLevel == maxHeight && currentShelf == maxLength) {
            throw new DataInputException("Warehouse is full. No additional bays can be added");
        }
        currentShelf++;
        if (currentShelf > maxLength) {
            currentShelf = 1;
            currentLevel++;
            if (currentLevel > maxHeight) {
                currentLevel = 1;
                currentRow++;
                if (currentRow > maxRows) {
                    throw new DataInputException("Warehouse is full. No additional rows can be added");
                }
            }
        }
        return currentRow + "-" + currentLevel + "-" + currentShelf;
    }

    public static Map<String, Integer> parseBayLabel(String bayLabel) throws Exception {
        Map<String, Integer> labelMap = new HashMap<>();
        int rowNumber;
        int shelfNumber;
        int levelNumber;
        String[] parts = bayLabel.split("-");

        if (parts.length != 3) {
            throw new Exception("Invalid bay label format.");
        }
        try {
            rowNumber = Integer.parseInt(parts[0]);
            levelNumber = Integer.parseInt(parts[1]);
            shelfNumber = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid bay label format: " + e.getMessage());
        }
        labelMap.put("rowNumber", rowNumber);
        labelMap.put("shelfNumber", shelfNumber);
        labelMap.put("levelNumber", levelNumber);
        return labelMap;
    }



}
