package com.ryangrillo.warehousemgt.api.service;

import com.ryangrillo.warehousemgt.api.enums.BayType;
import com.ryangrillo.warehousemgt.api.model.Bay;
import com.ryangrillo.warehousemgt.api.model.NewBay;
import com.ryangrillo.warehousemgt.api.model.Tag;
import com.ryangrillo.warehousemgt.api.model.Warehouse;
import com.ryangrillo.warehousemgt.api.repository.BayRepository;
import com.ryangrillo.warehousemgt.api.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
public class BayServiceTest {

    @Mock
    private BayRepository bayRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private BayService bayService;

    @Test
    public void testAddBayToWarehouse() throws Exception {
        NewBay newBay = new NewBay();
        newBay.setBayType(BayType.PALLET);
        newBay.setHoldingPoints(9);
        Tag tag = new Tag();
        tag.setName("tag");
        newBay.setTags(List.of(tag));
        String warehouseIdentifierCode = "123";
        Warehouse warehouse = new Warehouse();
        warehouse.setMaxLevels(10);
        warehouse.setMaxRows(10);
        warehouse.setMaxShelves(10);
        warehouse.setLastBayAdded("2-2-2");
        Mockito.when(warehouseRepository.findByIdentifierCode(eq(warehouseIdentifierCode)))
                .thenReturn(Optional.of(warehouse));
        Mockito.when(bayRepository.findLatestEntity()).thenReturn(new Bay());
        Bay result = bayService.addBayToWarehouse(newBay, warehouseIdentifierCode);
        assertNotNull(result);
        Mockito.verify(warehouseRepository, Mockito.times(1))
                .findByIdentifierCode(eq(warehouseIdentifierCode));
        Mockito.verify(bayRepository, Mockito.times(1)).save(any(Bay.class));
    }

    @Test
    public void testRemoveBayFromWarehouse() {
        String warehouseIdentifierCode = "123";
        Warehouse warehouse = new Warehouse();
        warehouse.setLastBayAdded("1-1-1");
        Mockito.when(warehouseRepository.findByIdentifierCode(eq(warehouseIdentifierCode)))
                .thenReturn(Optional.of(warehouse));
        bayService.removeBayFromWarehouse(warehouseIdentifierCode);
        Mockito.verify(bayRepository, Mockito.times(1))
                .deleteBayByBayLabelAndWarehouse(eq("1-1-1"), eq(warehouse));
    }

    @Test
    public void testUpdateWarehouseWithNewLastBayAdded() {
        String warehouseIdentifierCode = "123";
        Warehouse warehouse = new Warehouse();
        Bay bay = new Bay();
        List<Bay> bays = Arrays.asList(bay);
        Mockito.when(warehouseRepository.findByIdentifierCode(eq(warehouseIdentifierCode)))
                .thenReturn(Optional.of(warehouse));
        Mockito.when(bayRepository.findLatestEntity()).thenReturn(bay);
        Mockito.when(bayRepository.findAllByWarehouse(eq(warehouse))).thenReturn(bays);
        bayService.updateWarehouseWithNewLastBayAdded(warehouseIdentifierCode);
        Mockito.verify(warehouseRepository, Mockito.times(1))
                .updateLastBayAdded(eq(warehouse.getWarehouseId()), eq(bay.getBayLabel()));
    }

    @Test
    public void testGetBaysByWarehouseIdentifierCode() {
        String warehouseIdentifierCode = "123";
        Warehouse warehouse = new Warehouse();
        List<Bay> expectedBays = Arrays.asList(new Bay(), new Bay());
        Mockito.when(warehouseRepository.findByIdentifierCode(eq(warehouseIdentifierCode)))
                .thenReturn(Optional.of(warehouse));
        Mockito.when(bayRepository.findAllByWarehouse(eq(warehouse)))
                .thenReturn(expectedBays);
        List<Bay> result = bayService.getBaysByWarehouseIdentifierCode(warehouseIdentifierCode);
        assertEquals(expectedBays, result);
    }

    @Test
    public void testGetBaysByTags() {
        String warehouseIdentifierCode = "123";
        Warehouse warehouse = new Warehouse();
        List<String> tags = Arrays.asList("tag1", "tag2");
        List<String> expectedBayLabels = Arrays.asList("bay1", "bay2");
        Mockito.when(warehouseRepository.findByIdentifierCode(eq(warehouseIdentifierCode)))
                .thenReturn(Optional.of(warehouse));
        Mockito.when(bayRepository.findBayLabelsByTagsAndWarehouseId(eq(tags), eq(warehouse.getWarehouseId()), eq(tags.size())))
                .thenReturn(expectedBayLabels);
        List<String> result = bayService.getBaysByTags(tags, warehouseIdentifierCode);
        assertEquals(expectedBayLabels, result);
    }
}