package com.ryangrillo.warehousemgt.api.controller;


import com.ryangrillo.warehousemgt.api.model.Bay;
import com.ryangrillo.warehousemgt.api.model.HoldingPointsRequest;
import com.ryangrillo.warehousemgt.api.model.HoldingPointsStatus;
import com.ryangrillo.warehousemgt.api.model.NewBay;
import com.ryangrillo.warehousemgt.api.service.BayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BayControllerTest {

    @Mock
    private BayService bayService;

    @InjectMocks
    private BayController bayController;

    @Test
    public void testGetAllBaysByWarehouse() {
        String identifierCode = "123";
        List<Bay> expectedBays = Arrays.asList(new Bay(), new Bay());
        Mockito.when(bayService.getBaysByWarehouseIdentifierCode(anyString()))
                .thenReturn(expectedBays);
        List<Bay> result = bayController.getAllBaysbyWarehouse(identifierCode);
        assertEquals(expectedBays, result);
        Mockito.verify(bayService, Mockito.times(1))
                .getBaysByWarehouseIdentifierCode(identifierCode);
    }

    @Test
    public void testGetAllBaysByTags() {
        String identifierCode = "123";
        List<String> tags = Arrays.asList("tag1", "tag2");
        List<String> expectedBays = Arrays.asList("bay1", "bay2");
        Mockito.when(bayService.getBaysByTags(eq(tags), anyString()))
                .thenReturn(expectedBays);
        List<String> result = bayController.getAllBaysbyTags(identifierCode, tags);
        assertEquals(expectedBays, result);
        Mockito.verify(bayService, Mockito.times(1))
                .getBaysByTags(eq(tags), eq(identifierCode));
    }

    @Test
    public void testSaveBay() throws Exception {
        String identifierCode = "123";
        NewBay newBay = new NewBay();
        Bay expectedBay = new Bay();
        Mockito.when(bayService.addBayToWarehouse(eq(newBay), anyString()))
                .thenReturn(expectedBay);
        Bay result = bayController.saveBay(newBay, identifierCode);
        assertEquals(expectedBay, result);
        Mockito.verify(bayService, Mockito.times(1))
                .addBayToWarehouse(eq(newBay), eq(identifierCode));
    }

    @Test
    public void testDeleteBay() {
        String identifierCode = "123";
        bayController.deleteBay(identifierCode);
        Mockito.verify(bayService, Mockito.times(1))
                .removeBayFromWarehouse(eq(identifierCode));
        Mockito.verify(bayService, Mockito.times(1))
                .updateWarehouseWithNewLastBayAdded(eq(identifierCode));
    }

    @Test
    public void testAddHoldingPointsAvailableInBay() throws Exception {
        String identifierCode = "123";
        int quantity = 10;
        String bayLabel = "bay1";
        bayController.addHoldingPointsAvailableInBay(identifierCode, quantity, bayLabel);
        Mockito.verify(bayService, Mockito.times(1))
                .addHoldingPointsAvailable(eq(identifierCode), eq(quantity), eq(bayLabel));
    }

    @Test
    public void testSubtractHoldingPointsAvailableInBay() throws Exception {
        String identifierCode = "123";
        HoldingPointsRequest holdingPointsRequest = new HoldingPointsRequest();
        String bayLabel = "bay1";
        bayController.subtractHoldingPointsAvailableInBay(identifierCode, holdingPointsRequest, bayLabel);
        Mockito.verify(bayService, Mockito.times(1))
                .removeHoldingPointsAvailable(eq(identifierCode), eq(holdingPointsRequest.getQuantity()), eq(bayLabel));
    }

    @Test
    public void testGetTotalHoldingPointsAvailable() {
        String identifierCode = "123";
        String bayLabel = "bay1";
        HoldingPointsStatus expectedStatus = new HoldingPointsStatus();
        Mockito.when(bayService.getTotalHoldingPointsAvailable(eq(bayLabel), anyString()))
                .thenReturn(expectedStatus);
        HoldingPointsStatus result = bayController.getTotalHoldingPointsAvailable(bayLabel, identifierCode);
        assertEquals(expectedStatus, result);
        Mockito.verify(bayService, Mockito.times(1))
                .getTotalHoldingPointsAvailable(eq(bayLabel), eq(identifierCode));
    }
}


