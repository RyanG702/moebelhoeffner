package com.ryangrillo.warehousemgt.api.controller;

import com.ryangrillo.warehousemgt.api.model.Warehouse;
import com.ryangrillo.warehousemgt.api.service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class WarehouseControllerTest {

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private WarehouseController warehouseController;

    @Test
    public void testGetAllWareHouses() throws Exception {
        Warehouse warehouse1 = new Warehouse();
        warehouse1.setName("warehouse1");
        Warehouse warehouse2 = new Warehouse();
        warehouse2.setName("warehouse2");
        List<Warehouse> warehouses = Arrays.asList(warehouse1, warehouse2);
        when(warehouseService.getAllWarehouses()).thenReturn(warehouses);
        List<Warehouse> result = warehouseController.getAllWareHouses();
        assertEquals(result, warehouses);
    }

    @Test
    public void testGetWarehouseByIdentifierCode() {
        String identifierCode = "123";
        Warehouse expectedWarehouse = new Warehouse();
        expectedWarehouse.setName("warehouse1");
        expectedWarehouse.setIdentifierCode(identifierCode);
        Mockito.when(warehouseService.getWarehouseByIdentifierCode(anyString()))
                .thenReturn(Optional.of(expectedWarehouse));
        Optional<Warehouse> result = warehouseController.getWarehouseByIdentifierCode(identifierCode);
        assertEquals(expectedWarehouse, result.orElse(null));
        Mockito.verify(warehouseService, Mockito.times(1)).getWarehouseByIdentifierCode(identifierCode);
    }

    @Test
    public void testSaveWareHouse()  {
        Warehouse warehouse1 = new Warehouse();
        warehouse1.setName("warehouse1");
        when(warehouseService.saveWarehouse(any())).thenReturn(warehouse1);
        Warehouse result = warehouseController.saveWareHouse(warehouse1);
        assertEquals(result, warehouse1);
    }

    @Test
    public void testdeleteWarehouse()  {
        String identifierCode = "123";
        warehouseController.deleteWareHouse(identifierCode);
        Mockito.verify(warehouseService, Mockito.times(1)).deleteWarehouse(identifierCode);
    }

    @Test
    public void testUpdateWareHouseName() {
        String oldName = "Old Warehouse";
        String newName = "New Warehouse";
        Warehouse expectedWarehouse = new Warehouse();
        Mockito.when(warehouseService.updateWarehouseName(anyString(), anyString()))
                .thenReturn(Optional.of(expectedWarehouse));
        Optional<Warehouse> result = warehouseController.updateWareHouseName(oldName, newName);
        assertEquals(expectedWarehouse, result.orElse(null));
        Mockito.verify(warehouseService, Mockito.times(1))
                .updateWarehouseName(oldName, newName);
    }

    @Test
    public void testGetAllTagsByWarehouse() {
        String identifierCode = "123";
        List<String> expectedTags = Arrays.asList("tag1", "tag2", "tag3");
        Mockito.when(warehouseService.getAllTagsByWarehouse(anyString()))
                .thenReturn(expectedTags);
        List<String> result = warehouseController.getAllTagsByWarehouse(identifierCode);
        assertEquals(expectedTags, result);
        Mockito.verify(warehouseService, Mockito.times(1))
                .getAllTagsByWarehouse(identifierCode);
    }



}