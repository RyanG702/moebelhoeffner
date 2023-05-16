package com.ryangrillo.warehousemgt.api.controller;

import com.ryangrillo.warehousemgt.api.model.Warehouse;
import com.ryangrillo.warehousemgt.api.repository.WarehouseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class WarehouseController {

    @Autowired
    WarehouseRepository warehouseRepository;

    @PostMapping(path = {"/warehouse"})
    public Warehouse saveWareHouse(@Valid @RequestBody Warehouse warehouse ) {
        warehouseRepository.save(warehouse);
        return warehouse;
    }
}
