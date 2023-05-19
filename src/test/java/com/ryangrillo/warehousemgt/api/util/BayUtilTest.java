package com.ryangrillo.warehousemgt.api.util;

import com.ryangrillo.warehousemgt.api.exception.DataInputException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BayUtilTest {

    @Test
    public void testCreateNewBayLabel_NotFull() throws Exception {
        int currentRow = 1;
        int currentShelf = 1;
        int currentLevel = 1;
        int maxRows = 3;
        int maxLength = 3;
        int maxHeight = 3;
        String bayLabel = BayUtil.createNewBayLabel(currentRow, currentShelf, currentLevel, maxRows, maxLength, maxHeight);
        assertNotNull(bayLabel);
        assertEquals("1-1-2", bayLabel);
    }
    @Test
    public void testCreateNewBayLabel_FullWarehouse() {
        int currentRow = 3;
        int currentShelf = 3;
        int currentLevel = 3;
        int maxRows = 3;
        int maxLength = 3;
        int maxHeight = 3;
        Exception exception = assertThrows(DataInputException.class, () -> {
            BayUtil.createNewBayLabel(currentRow, currentShelf, currentLevel, maxRows, maxLength, maxHeight);
        });
        assertEquals("Warehouse is full. No additional bays can be added", exception.getMessage());

}
}
