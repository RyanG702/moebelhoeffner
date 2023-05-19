package com.ryangrillo.warehousemgt.api.repository;

import com.ryangrillo.warehousemgt.api.model.Bay;
import com.ryangrillo.warehousemgt.api.model.Tag;
import com.ryangrillo.warehousemgt.api.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BayRepository  extends JpaRepository<Bay, Long > {
    List<Bay> findAllByWarehouse(Warehouse warehouse);

    Bay findByWarehouseAndBayLabel(Warehouse warehouse, String bayLabel);

    @Transactional
    @Modifying
    void deleteBayByBayLabelAndWarehouse(String bayLabel, Warehouse warehouse);

    @Query("SELECT e FROM Bay e WHERE e.timestamp = (SELECT MAX(e2.timestamp) FROM Bay e2)")
    Bay findLatestEntity();

    @Query("SELECT b.bayLabel FROM Bay b JOIN b.tags t WHERE b.warehouse.id = :warehouseId AND t.name IN :tagNames GROUP BY b.bayLabel HAVING COUNT(DISTINCT t.name) = :tagCount")
    List<String> findBayLabelsByTagsAndWarehouseId(@Param("tagNames") List<String> tagNames, @Param("warehouseId") Long warehouseId, @Param("tagCount") int tagCount);

    @Transactional
    @Modifying
    @Query("UPDATE Bay b SET b.holdingPointsUsed = b.holdingPointsUsed - :quantity WHERE b.bayLabel = :bayLabel AND b.warehouse = :warehouse")
    void addHoldingPointsUsed(@Param("bayLabel") String bayLabel, @Param("quantity") int quantity, @Param("warehouse") Warehouse warehouse);

    @Transactional
    @Modifying
    @Query("UPDATE Bay b SET b.holdingPointsUsed = b.holdingPointsUsed + :quantity WHERE b.bayLabel = :bayLabel AND b.warehouse = :warehouse")
    void removeHoldingPointsUsed(@Param("bayLabel") String bayLabel, @Param("quantity") int quantity, @Param("warehouse") Warehouse warehouse);
    @Query("SELECT DISTINCT t.name FROM Bay b JOIN b.tags t WHERE b.warehouse = :warehouse")
    List<String> findAllTagsByWarehouse(@Param("warehouse") Warehouse warehouse);
}