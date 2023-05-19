package com.ryangrillo.warehousemgt.api.controller;

import com.ryangrillo.warehousemgt.api.model.Bay;
import com.ryangrillo.warehousemgt.api.model.HoldingPointsRequest;
import com.ryangrillo.warehousemgt.api.model.HoldingPointsStatus;
import com.ryangrillo.warehousemgt.api.model.NewBay;
import com.ryangrillo.warehousemgt.api.service.BayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BayController {

    @Autowired
    BayService bayService;

    @GetMapping(path = {"/warehouses/{identifierCode}/bays"})
    public List<Bay> getAllBaysbyWarehouse(@PathVariable String identifierCode) {
        return bayService.getBaysByWarehouseIdentifierCode(identifierCode);
    }

    @GetMapping(path = {"/warehouses/{identifierCode}/bays/byTags"})
    public List<String> getAllBaysbyTags(@PathVariable String identifierCode, @RequestParam List<String> tags) {
        return bayService.getBaysByTags(tags,identifierCode );
    }


    @PostMapping(path = {"/warehouses/{identifierCode}/bays"})
    public Bay saveBay(@RequestBody NewBay newBay, @PathVariable String identifierCode) throws Exception {
        return bayService.addBayToWarehouse(newBay, identifierCode);
    }

    @DeleteMapping(path = {"/warehouses/{identifierCode}/bays"})
    public void deleteBay(@PathVariable String identifierCode) {
         bayService.removeBayFromWarehouse(identifierCode);
         bayService.updateWarehouseWithNewLastBayAdded(identifierCode);
    }

    @DeleteMapping(path = {"/warehouses/{identifierCode}/bays/{bayLabel}/busyHoldingPoints/{quantity}"})
    public void addHoldingPointsAvailableInBay(@PathVariable String identifierCode, @PathVariable int quantity, @PathVariable String bayLabel) throws Exception {
        bayService.addHoldingPointsAvailable(identifierCode, quantity, bayLabel);
    }

    @PostMapping(path = {"/warehouses/{identifierCode}/bays/{bayLabel}/busyHoldingPoints"})
    public void subtractHoldingPointsAvailableInBay(@PathVariable String identifierCode, @RequestBody HoldingPointsRequest holdingPointsRequest, @PathVariable String bayLabel) throws Exception {
        bayService.removeHoldingPointsAvailable(identifierCode, holdingPointsRequest.getQuantity(), bayLabel);
    }

    @GetMapping(path = {"/warehouses/{identifierCode}/bays/{bayLabel}/holdingPoints/status"})
    public HoldingPointsStatus getTotalHoldingPointsAvailable(@PathVariable String bayLabel, @PathVariable String identifierCode) {
        return bayService.getTotalHoldingPointsAvailable(bayLabel, identifierCode);
    }

}
