package com.ryangrillo.warehousemgt.api.service;

import com.ryangrillo.warehousemgt.api.exception.DataFetchException;
import com.ryangrillo.warehousemgt.api.exception.EntityAlreadyExistsException;
import com.ryangrillo.warehousemgt.api.model.Warehouse;
import com.ryangrillo.warehousemgt.api.repository.BayRepository;
import com.ryangrillo.warehousemgt.api.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private BayRepository bayRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    @Test
    public void testSaveWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setIdentifierCode("123");
        Mockito.when(warehouseRepository.save(warehouse)).thenReturn(warehouse);
        Warehouse result = warehouseService.saveWarehouse(warehouse);
        assertNotNull(result);
        Mockito.verify(warehouseRepository, Mockito.times(1)).save(warehouse);
    }

    @Test
    public void testSaveWarehouse_AlreadyExists() {
        Warehouse warehouse = new Warehouse();
        warehouse.setIdentifierCode("123");
        Mockito.when(warehouseRepository.save(warehouse)).thenThrow(DataIntegrityViolationException.class);
        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> {
            warehouseService.saveWarehouse(warehouse);
        });
        assertEquals("Warehouse already exists.", exception.getMessage());
    }

    @Test
    public void testDeleteWarehouse() {
        String identifierCode = "123";
        Warehouse warehouse = new Warehouse();
        Mockito.when(warehouseRepository.findByIdentifierCode(identifierCode)).thenReturn(Optional.of(warehouse));
        Mockito.when(bayRepository.findAllByWarehouse(warehouse)).thenReturn(Collections.emptyList());
        warehouseService.deleteWarehouse(identifierCode);
        Mockito.verify(warehouseRepository, Mockito.times(1)).deleteByIdentifierCode(identifierCode);
    }

    @Test
    public void testDeleteWarehouse_NotFound() {
        String identifierCode = "123";
        Mockito.when(warehouseRepository.findByIdentifierCode(identifierCode)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            warehouseService.deleteWarehouse(identifierCode);
        });
        assertEquals("Warehouse not found.", exception.getMessage());
    }

    @Test
    public void testUpdateWarehouseName() {
        String oldName = "Old Name";
        String newName = "New Name";
        Warehouse warehouse = new Warehouse();
        warehouse.setName(oldName);
        Mockito.when(warehouseRepository.findByName(oldName)).thenReturn(Optional.of(warehouse));
        Mockito.when(warehouseRepository.save(warehouse)).thenReturn(warehouse);
        Optional<Warehouse> result = warehouseService.updateWarehouseName(oldName, newName);
        assertTrue(result.isPresent());
        assertEquals(newName, result.get().getName());
        Mockito.verify(warehouseRepository, Mockito.times(1)).save(warehouse);
    }

    @Test
    public void testUpdateWarehouseName_NotFound() {
        String oldName = "Old Name";
        String newName = "New Name";
        Mockito.when(warehouseRepository.findByName(oldName)).thenReturn(Optional.empty());
        Optional<Warehouse> result = warehouseService.updateWarehouseName(oldName, newName);
        assertFalse(result.isPresent());
        Mockito.verify(warehouseRepository, Mockito.never()).save(Mockito.any(Warehouse.class));
    }



    @Test
    public void testGetWarehouseByIdentifierCode() {
        String identifierCode = "123";
        Warehouse warehouse = new Warehouse();
        Mockito.when(warehouseRepository.findByIdentifierCode(identifierCode)).thenReturn(Optional.of(warehouse));
        Optional<Warehouse> result = warehouseService.getWarehouseByIdentifierCode(identifierCode);
        assertTrue(result.isPresent());
        assertEquals(warehouse, result.get());
    }

    @Test
    public void testGetWarehouseByIdentifierCode_NotFound() {
        String identifierCode = "123";
        Mockito.when(warehouseRepository.findByIdentifierCode(identifierCode)).thenReturn(Optional.empty());
        Optional<Warehouse> result = warehouseService.getWarehouseByIdentifierCode(identifierCode);
        assertFalse(result.isPresent());
    }


}
