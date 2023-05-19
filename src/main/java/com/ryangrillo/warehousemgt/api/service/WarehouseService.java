package com.ryangrillo.warehousemgt.api.service;

import com.ryangrillo.warehousemgt.api.exception.DataFetchException;
import com.ryangrillo.warehousemgt.api.exception.EntityAlreadyExistsException;
import com.ryangrillo.warehousemgt.api.exception.Validator;
import com.ryangrillo.warehousemgt.api.model.Bay;
import com.ryangrillo.warehousemgt.api.model.Warehouse;
import com.ryangrillo.warehousemgt.api.repository.BayRepository;
import com.ryangrillo.warehousemgt.api.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    BayRepository bayRepository;

    public Warehouse saveWarehouse(Warehouse warehouse) {
        Validator.validateWarehouseId(warehouse.getIdentifierCode());
        try {
            warehouseRepository.save(warehouse);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException("Warehouse already exists.");
        }
        return warehouse;
    }
    public void deleteWarehouse(String identifierCode) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdentifierCode(identifierCode);
        Warehouse warehouse = optionalWarehouse.orElseThrow(() -> new RuntimeException("Warehouse not found."));
        List<Bay> warehouses = bayRepository.findAllByWarehouse(warehouse);
        if(!warehouses.isEmpty()) {
            throw new DataFetchException("cannot delete warehouse that has bays in it");
        }
        warehouseRepository.deleteByIdentifierCode(identifierCode);
    }

    public Optional<Warehouse> updateWarehouseName(String oldName, String newName) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByName(oldName);
        optionalWarehouse.ifPresent(warehouse -> {
            warehouse.setName(newName);
            warehouseRepository.save(warehouse);
        });
        return optionalWarehouse;
    }


    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        if (warehouses.isEmpty()) {
            throw new DataFetchException("No warehouses found.");
        }
        return warehouses;
    }

    public Optional<Warehouse> getWarehouseByIdentifierCode(String identifierCode) {
        return warehouseRepository.findByIdentifierCode(identifierCode);
    }

    public List<String> getAllTagsByWarehouse(String warehouseIdentifierCode ) {
        Warehouse warehouse = warehouseRepository.findByIdentifierCode(warehouseIdentifierCode)
                .orElseThrow(() -> new DataFetchException("Warehouse not found."));
        return bayRepository.findAllTagsByWarehouse(warehouse);
    }
}