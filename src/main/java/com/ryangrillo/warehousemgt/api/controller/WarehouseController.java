package com.ryangrillo.warehousemgt.api.controller;

import com.ryangrillo.warehousemgt.api.model.Tag;
import com.ryangrillo.warehousemgt.api.model.Warehouse;
import com.ryangrillo.warehousemgt.api.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class WarehouseController {

    @Autowired
    WarehouseService warehouseService;

    @GetMapping(path = {"/warehouses"})
    public List<Warehouse> getAllWareHouses() {
        return warehouseService.getAllWarehouses();
    }

    @GetMapping(path = {"/warehouse/{identiferCode}"})
    public Optional<Warehouse> getWarehouseByIdentifierCode(@PathVariable String identiferCode) {
        return warehouseService.getWarehouseByIdentifierCode(identiferCode);
    }

    @PostMapping(path = {"/warehouses"})
    public Warehouse saveWareHouse(@Valid @RequestBody Warehouse warehouse ) {
        return warehouseService.saveWarehouse(warehouse);
    }

    @DeleteMapping(path = {"/warehouses"})
    public void deleteWareHouse(String identifierCode ) {
        warehouseService.deleteWarehouse(identifierCode);
    }

    @PutMapping(path = {"/warehouses/{oldName}/name/{newName}"})
    public Optional<Warehouse> updateWareHouseName(@PathVariable String oldName, @PathVariable String newName ) {
        return warehouseService.updateWarehouseName(oldName, newName);
    }

    @GetMapping(path = {"/warehouse/tags/{identiferCode}"})
    public List<String> getAllTagsByWarehouse(@PathVariable String identiferCode) {
        return warehouseService.getAllTagsByWarehouse(identiferCode);
    }
}