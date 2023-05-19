package com.ryangrillo.warehousemgt.api.service;

import com.ryangrillo.warehousemgt.api.enums.BayType;
import com.ryangrillo.warehousemgt.api.exception.DataFetchException;
import com.ryangrillo.warehousemgt.api.exception.DataInputException;
import com.ryangrillo.warehousemgt.api.exception.Validator;
import com.ryangrillo.warehousemgt.api.model.*;
import com.ryangrillo.warehousemgt.api.repository.BayRepository;
import com.ryangrillo.warehousemgt.api.repository.WarehouseRepository;
import com.ryangrillo.warehousemgt.api.util.BayUtil;
//import com.ryangrillo.warehousemgt.api.util.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BayService {

    @Autowired
    BayRepository bayRepository;

    @Autowired
    WarehouseRepository warehouseRepository;



    @Transactional
    public Bay addBayToWarehouse(NewBay newBay, String warehouseIdentifierCode) throws Exception {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdentifierCode(warehouseIdentifierCode);
        Warehouse warehouse = optionalWarehouse.orElseThrow(() -> new DataFetchException("Warehouse not found."));
        BayType bayType = newBay.getBayType();
        int rowNumber = 1;
        int shelfNumber = 1;
        int levelNumber = 1;
        String bayLabel;
        if (warehouse.getLastBayAdded() != null) {
            Map<String, Integer> bayAttribues = BayUtil.parseBayLabel(warehouse.getLastBayAdded());
             rowNumber = bayAttribues.get("rowNumber");
             shelfNumber = bayAttribues.get("shelfNumber");
             levelNumber = bayAttribues.get("levelNumber");
            bayLabel = BayUtil.createNewBayLabel(rowNumber, shelfNumber, levelNumber,warehouse.getMaxRows(), warehouse.getMaxShelves(), warehouse.getMaxLevels());
            warehouse.setLastBayAdded(bayLabel);
        } else {
            warehouse.setLastBayAdded("1-1-1");
            bayLabel = "1-1-1";
        }
         Map<String, Integer> newBayAttributes = BayUtil.parseBayLabel(bayLabel);
        Validator.validateHoldingPoints(newBay.getHoldingPoints());
        Validator.validateTags(newBay.getTags());
         Bay bay = new Bay(String.valueOf(newBayAttributes.get("rowNumber")), String.valueOf(newBayAttributes.get("shelfNumber")), String.valueOf(newBayAttributes.get("levelNumber")), bayLabel, newBay.getTags(), bayType, newBay.getHoldingPoints(), warehouse);
         warehouseRepository.save(warehouse);
         bayRepository.save(bay);
        return bay;
    }


    public void removeBayFromWarehouse(String warehouseIdentifierCode) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdentifierCode(warehouseIdentifierCode);
        Warehouse warehouse = optionalWarehouse.orElseThrow(() -> new RuntimeException("Warehouse not found."));
        bayRepository.deleteBayByBayLabelAndWarehouse(warehouse.getLastBayAdded(), warehouse);
    }

    @Transactional
    public void updateWarehouseWithNewLastBayAdded(String warehouseIdentifierCode) {
         Warehouse warehouse = warehouseRepository.findByIdentifierCode(warehouseIdentifierCode)
                .orElseThrow(() -> new DataFetchException("Warehouse not found."));
        Bay bay = bayRepository.findLatestEntity();
        List<Bay> bays = bayRepository.findAllByWarehouse(warehouse);
        warehouseRepository.updateLastBayAdded(warehouse.getWarehouseId(), bays.size() == 0? null : bay.getBayLabel());
    }


    public List<Bay> getBaysByWarehouseIdentifierCode(String warehouseIdentifierCode) {
        Warehouse warehouse = warehouseRepository.findByIdentifierCode(warehouseIdentifierCode)
                .orElseThrow(() -> new DataFetchException("Warehouse not found."));
        return bayRepository.findAllByWarehouse(warehouse);
    }

    public List<String> getBaysByTags(List<String> tags, String warehouseIdentifierCode ) {
        Warehouse warehouse = warehouseRepository.findByIdentifierCode(warehouseIdentifierCode)
                .orElseThrow(() -> new DataFetchException("Warehouse not found."));
        return bayRepository.findBayLabelsByTagsAndWarehouseId(tags, warehouse.getWarehouseId(), tags.size());
    }

    public void addHoldingPointsAvailable(String warehouseIdentifierCode, int quantity, String bayLabel) {
        Warehouse warehouse = warehouseRepository.findByIdentifierCode(warehouseIdentifierCode)
                .orElseThrow(() -> new DataFetchException("Warehouse not found."));
        Bay bay = bayRepository.findByWarehouseAndBayLabel(warehouse, bayLabel);
        if (quantity > (bay.getHoldingPointsUsed())) {
            throw new DataInputException("there are only " + bay.getHoldingPointsUsed() + " busy holding points");
        }
        bayRepository.addHoldingPointsUsed(bayLabel, quantity, warehouse);
    }

    public void removeHoldingPointsAvailable(String warehouseIdentifierCode, int quantity, String bayLabel) {
        Warehouse warehouse = warehouseRepository.findByIdentifierCode(warehouseIdentifierCode)
                .orElseThrow(() -> new DataFetchException("Warehouse not found."));
        Bay bay = bayRepository.findByWarehouseAndBayLabel(warehouse, bayLabel);
        int holdingPointFree = bay.getHoldingPoints() - bay.getHoldingPointsUsed();
        if (quantity > holdingPointFree) {
            throw new DataInputException("there are only " + holdingPointFree + " free holding points available");
        }
        bayRepository.removeHoldingPointsUsed(bayLabel, quantity, warehouse);
    }

    public HoldingPointsStatus getTotalHoldingPointsAvailable(String bayLabel, String warehouseIdentifierCode ) {
        Warehouse warehouse = warehouseRepository.findByIdentifierCode(warehouseIdentifierCode)
                .orElseThrow(() -> new DataFetchException("Warehouse not found."));
        Bay bay = bayRepository.findByWarehouseAndBayLabel(warehouse, bayLabel);
        int holdingPointsTotal = bay.getHoldingPoints();
        int holdingPointsUsed = bay.getHoldingPointsUsed();
        HoldingPointsStatus holdingPointsStatus = new HoldingPointsStatus(holdingPointsTotal, holdingPointsUsed);
        return holdingPointsStatus;
    }

}
